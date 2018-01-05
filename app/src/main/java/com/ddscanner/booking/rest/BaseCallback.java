package com.ddscanner.booking.rest;


import com.ddscanner.booking.models.errors.Field;
import com.ddscanner.booking.models.errors.GeneralError;
import com.ddscanner.booking.models.errors.ValidationError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract class BaseCallback<T> implements Callback<ResponseBody> {
    private WeakReference<DDScannerRestClient.ResultListener<T>> resultListenerWeakReference;
    private Gson gson;

    BaseCallback(Gson gson, DDScannerRestClient.ResultListener<T> resultListener) {
        this.gson = gson;
        this.resultListenerWeakReference = new WeakReference<>(resultListener);
    }

    @Override
    public final void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (resultListenerWeakReference.get() != null && !resultListenerWeakReference.get().isCancelled()) {
            onResponse(resultListenerWeakReference.get(), call, response);
        }
    }

    @Override
    public final void onFailure(Call<ResponseBody> call, Throwable t) {
        if (resultListenerWeakReference.get() != null && !resultListenerWeakReference.get().isCancelled()) {
            if (t instanceof ConnectException) {
                resultListenerWeakReference.get().onConnectionFailure();
            } else if (t instanceof SocketTimeoutException) {
                resultListenerWeakReference.get().onConnectionFailure();
            } else {
                resultListenerWeakReference.get().onError(DDScannerRestClient.ErrorType.UNKNOWN_ERROR, null, call.request().url().toString(), t.getMessage());
            }
        }
    }

    abstract void onResponse(DDScannerRestClient.ResultListener<T> resultListener, Call<ResponseBody> call, Response<ResponseBody> response);

    void checkForError(Call<ResponseBody> call, int responseCode, String json, DDScannerRestClient.ResultListener resultListener) {
        // Because this method is called inside onResponse or onFailure only after check that (resultListenerWeakReference.get() != null) we do not need to check resultListener for nullPointer
        GeneralError generalError;
        ValidationError validationError;
        switch (responseCode) {
            case 400:
                // bad request. for example event already happened or event preconditions are not held
                try {
                    generalError = gson.fromJson(json, GeneralError.class);
                    resultListener.onError(DDScannerRestClient.ErrorType.BAD_REQUEST_ERROR_400, generalError, call.request().url().toString(), json);
                } catch (JsonSyntaxException e) {
                    resultListener.onError(DDScannerRestClient.ErrorType.JSON_SYNTAX_EXCEPTION, null, call.request().url().toString(), e.getMessage());
                }
                break;
            case 401:

                try {
                    generalError = gson.fromJson(json, GeneralError.class);
//                    resultListener.onError(DDScannerRestClient.ErrorType.UNAUTHORIZED_401, generalError, call.request().url().toString(), generalError.getMessage());
                } catch (JsonSyntaxException e) {
//                    resultListener.onError(DDScannerRestClient.ErrorType.JSON_SYNTAX_EXCEPTION, null, call.request().url().toString(), e.getMessage());
                }
                break;
            case 403:
                try {
                    generalError = gson.fromJson(json, GeneralError.class);
                    resultListener.onError(DDScannerRestClient.ErrorType.RIGHTS_NOT_FOUND_403, generalError, call.request().url().toString(), generalError.getMessage());
                } catch (JsonSyntaxException e) {
                    resultListener.onError(DDScannerRestClient.ErrorType.JSON_SYNTAX_EXCEPTION, null, call.request().url().toString(), e.getMessage());
                }
                break;
            case 404:
                try {
                    generalError = gson.fromJson(json, GeneralError.class);
                    resultListener.onError(DDScannerRestClient.ErrorType.ENTITY_NOT_FOUND_404, generalError, call.request().url().toString(), generalError.getMessage());
                } catch (JsonSyntaxException e) {
                    resultListener.onError(DDScannerRestClient.ErrorType.JSON_SYNTAX_EXCEPTION, null, call.request().url().toString(), e.getMessage());
                }
                break;
            case 422:
                // unprocessable entity error, aka validation error
                try {
                    validationError = new ValidationError();
                    JsonElement jsonElement = new JsonParser().parse(json);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                    Field field;
                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                        field = new Field();
                        field.setName(entry.getKey());
                        JsonArray jsonArray = entry.getValue().getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            field.addError(jsonArray.get(i).getAsString());
                        }
                        validationError.addField(field);
                    }
                    resultListener.onError(DDScannerRestClient.ErrorType.UNPROCESSABLE_ENTITY_ERROR_422, validationError, call.request().url().toString(), json);
                } catch (JsonSyntaxException e) {
                    resultListener.onError(DDScannerRestClient.ErrorType.JSON_SYNTAX_EXCEPTION, null, call.request().url().toString(), e.getMessage());
                }
                break;
            case 500:
                // unknown server error
                try {
                    generalError = gson.fromJson(json, GeneralError.class);
                    resultListener.onError(DDScannerRestClient.ErrorType.SERVER_INTERNAL_ERROR_500, generalError, call.request().url().toString(), generalError.getMessage());
                } catch (JsonSyntaxException e) {
                    resultListener.onError(DDScannerRestClient.ErrorType.JSON_SYNTAX_EXCEPTION, null, call.request().url().toString(), e.getMessage());
                }
                break;
            case 409:
                try {
                    generalError = gson.fromJson(json, GeneralError.class);
                    resultListener.onError(DDScannerRestClient.ErrorType.DATA_ALREADY_EXIST_409, generalError, call.request().url().toString(), generalError.getMessage());
                } catch (JsonSyntaxException e) {
                    resultListener.onError(DDScannerRestClient.ErrorType.JSON_SYNTAX_EXCEPTION, null, call.request().url().toString(), e.getMessage());
                }
                break;
            default:
                // If unexpected error code is received
                try {
                    generalError = gson.fromJson(json, GeneralError.class);
                    resultListener.onError(DDScannerRestClient.ErrorType.UNKNOWN_ERROR, null, call.request().url().toString(), generalError.getMessage());
                } catch (JsonSyntaxException e) {
                    resultListener.onError(DDScannerRestClient.ErrorType.JSON_SYNTAX_EXCEPTION, null, call.request().url().toString(), e.getMessage());
                } catch (NullPointerException ignored) {
                    
                }
                break;
        }
    }
}
