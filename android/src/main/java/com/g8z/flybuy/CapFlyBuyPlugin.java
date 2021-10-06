package com.g8z.flybuy;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;
import com.radiusnetworks.flybuy.sdk.FlyBuyCore;
import com.radiusnetworks.flybuy.sdk.data.common.SdkError;
import com.radiusnetworks.flybuy.sdk.data.customer.CustomerInfo;
import com.radiusnetworks.flybuy.sdk.data.customer.CustomerState;
import com.radiusnetworks.flybuy.sdk.data.order.OrderState;
import com.radiusnetworks.flybuy.sdk.data.room.domain.Customer;
import com.radiusnetworks.flybuy.sdk.data.room.domain.Order;
import com.radiusnetworks.flybuy.sdk.data.room.domain.PickupWindow;
import com.radiusnetworks.flybuy.sdk.pickup.PickupManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.threeten.bp.Instant;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CapacitorPlugin(name = "CapFlyBuy")
public class CapFlyBuyPlugin extends Plugin {

  private static PluginCall eventsCallbackContext;
  private FlyBuyEventObserver orderObserver = new FlyBuyEventObserver();
  private ArrayList<Integer> observedIds = new ArrayList<>();
  Gson gson;

  @PluginMethod
  public void echo(PluginCall call) {
    String value = null;
    try {
      value = call.getArray("value").getString(0);
      JSObject ret = new JSObject();
      ret.put("status", "ok");
      ret.put("messageAs", value);
      call.resolve(ret);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void load() {
    String apiToken = this.getConfig().getString("flybuyAndroidApiKey");
    FlyBuyCore.configure(this.getContext(), apiToken);
    PickupManager.Companion.getInstance(null).configure(this.getContext());
    PickupManager.Companion.getInstance(null).onLocationPermissionChanged();

    this.gson = new GsonBuilder().disableHtmlEscaping()
        .registerTypeAdapter(Instant.class, new JsonSerializer<Instant>() {
          public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(instant.toString());
          }
        }).create();
  }

  public void onCreate(final JSONArray args, final PluginCall call) throws JSONException {
    final String apiKey = args.getString(0);
    final Activity activity = this.getActivity();
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      public void run() {
        FlyBuyCore.configure(activity, apiKey);
        PickupManager.Companion.getInstance(null).configure(getContext());
        JSObject ret = new JSObject();
        ret.put("status", "ok");
        call.resolve(ret);
      }
    });
  }

  public void onActivityStarted(final PluginCall call) {
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      public void run() {
        JSObject ret = new JSObject();
        ret.put("status", "ok");
        call.resolve(ret);
      }
    });
  }

  private void removeObservers() {
    for (Integer id : observedIds) {
      FlyBuyCore.orders.getOrder(id).removeObserver(orderObserver);
    }
    observedIds.clear();
  }

  private void sendResult(Object result, SdkError error, PluginCall call) {
    sendResult(result, error, call, false);
  }

  private void sendResult(Object result, SdkError error, PluginCall call, boolean keepCallback) {
    JSObject ret = new JSObject();
    if (error == null) {
      try {
        String jsonStr = gson.toJson(result);
        if (result == null) {
          ret.put("status", "ok");
        } else if (result instanceof List<?>) {
          ret.put("status", "ok");
          ret.put("messageAs", new JSONArray(jsonStr));
        } else {
          ret.put("status", "ok");
          ret.put("messageAs", new JSONObject(jsonStr));
        }
      } catch (JSONException e) {
//                Timber.e(e);
        ret.put("status", "error");
        ret.put("messageAs", e.getMessage());
      }
    } else {
//            Timber.e(error.getType().toString());
      ret.put("status", "error");
      ret.put("messageAs", gson.toJson(error));
    }

//        pluginResult.setKeepCallback(keepCallback);
    call.resolve(ret);
  }

  public class FlyBuyEventObserver implements Observer<Order> {
    @Override
    public void onChanged(Order order) {
      if (CapFlyBuyPlugin.eventsCallbackContext != null) {
        sendResult(order, null, CapFlyBuyPlugin.eventsCallbackContext, true);
      }
    }
  }

  public void onActivityStopped(final PluginCall call) {
    removeObservers();
    CapFlyBuyPlugin.eventsCallbackContext = null;
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      public void run() {
        JSObject ret = new JSObject();
        ret.put("status", "ok");
        call.resolve(ret);
      }
    });
  }

  public void onLocationPermissionChanged(final PluginCall call) {
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      public void run() {
        JSObject ret = new JSObject();
        ret.put("status", "ok");
        call.resolve(ret);
      }
    });
  }

  public void fetchOrders(final PluginCall call) {
    FlyBuyCore.orders.fetch((orders, error) -> {
      sendResult(orders, error, call);
      return null;
    });
  }

  public void getAllSites(final JSONArray args, final PluginCall call) {
    String query = "";
    if (args != null) {
      try {
        query = args.getString(0);
      } catch (JSONException ignored) {
      }
    }

    FlyBuyCore.sites.fetchAll(query, (sites, error) -> {
      sendResult(sites, error, call);
      return null;
    });
  }

  public void getConfig(final PluginCall call) {
    FlyBuyCore.config.fetch(true, (data, error) -> {
      sendResult(data, error, call);
      return null;
    });
  }

  public void claimOrder(final JSONArray args, final PluginCall call) {
    try {
      String redemptionCode = args.getString(0);

      // Fetch all orders and search for the one with the passed in redemption code.
      FlyBuyCore.orders.fetch((orders, fetchOrdersError) -> {

        Order foundOrder = null;
        if (orders != null) {
          for (Order order : orders) {
            if (redemptionCode.equals(order.getRedemptionCode())) {
              foundOrder = order;
            }
          }
        }

        if (foundOrder != null) { // If order is found attempt to claim it.
          claimFetchedOrder(foundOrder, call);
        } else { // Order not found. Fetch it by redemption code
          FlyBuyCore.orders.fetch(redemptionCode, (fetchedOrder, sdkError) -> {
            if (sdkError != null) { // Return error
              sendResult(fetchedOrder, sdkError, call);
            } else { // Now claim the order
              claimFetchedOrder(fetchedOrder, call);
            }
            return null;
          });
        }
        return null;
      });

    } catch (JSONException exception) {
//            Timber.e(exception, "Error claiming order with redemption code")
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", exception.getMessage());
      call.resolve(ret);
    }
  }

  private void claimFetchedOrder(Order order, PluginCall call) {
    if (order == null || TextUtils.isEmpty(order.getRedemptionCode())) {
//            Timber.e("Error claiming order with redemption code");
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", "Error claiming order with redemption code");
      call.resolve(ret);
      return;
    }

    CustomerInfo customerInfo = new CustomerInfo(order.getCustomer().getName(), order.getCustomer().getPhone(),
        order.getCustomer().getCarType(), order.getCustomer().getCarColor(), order.getCustomer().getLicensePlate());

    // If we haven't established a customer with the SDK yet, we have to do that
    // before claiming an order.
    // Only do this if `currentCustomer` isn't initialized yet.
    if (FlyBuyCore.customer.getCurrent() == null) {
      FlyBuyCore.customer.create(customerInfo, true, true, null, null, (customerInfoResult, sdkError) -> {
        if (sdkError != null) {
          sendResult(customerInfoResult, sdkError, call);
        } else {
          doClaim(order, customerInfo, call);
        }
        return null;
      });
    } else {
      doClaim(order, customerInfo, call);
    }
  }

  public void doClaim(Order order, CustomerInfo customerInfo, PluginCall call) {
    if (order == null || TextUtils.isEmpty(order.getRedemptionCode())) {
//            Timber.e("Error claiming order with redemption code");
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", "Error claiming order with redemption code");
      call.resolve(ret);
      return;
    }

    FlyBuyCore.orders.claim(order.getRedemptionCode(), customerInfo, order.getPickupType(),
        (claimedOrder, claimedError) -> {
          sendResult(claimedOrder, claimedError, call);
          return null;
        });
  }

  public void createCustomer(final JSONArray args, final PluginCall call) {
    final CustomerInfo customerInfo;
    // Build customer object from json
    try {
      JSONObject customerJSON = args.getJSONObject(0);

      customerInfo = new CustomerInfo(customerJSON.getString("name"), customerJSON.getString("phone"),
          customerJSON.getString("carType"), customerJSON.getString("carColor"),
          customerJSON.getString("licensePlate"));

    } catch (JSONException exception) {
//            Timber.e(exception, "Error parsing customer JSON");
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", exception.getMessage());
      call.resolve(ret);
      return;
    }

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        // Create the customer in flybuy
        FlyBuyCore.customer.create(customerInfo, true, true, null, null, (customerInfoResult, sdkError) -> {
          sendResult(customerInfoResult, sdkError, call);
          return null;
        });
      }
    });
    thread.start();
  }

  public void getPermissionsStatus(final JSONArray args, final PluginCall call) throws JSONException {
    String str;
    boolean foreground = super.hasPermission("android.permission.ACCESS_FINE_LOCATION");
    if (Build.VERSION.SDK_INT >= 29) {
      if (foreground) {
        boolean background = super.hasPermission("android.permission.ACCESS_BACKGROUND_LOCATION");
        str = background ? "GRANTED_BACKGROUND" : "GRANTED_FOREGROUND";
      } else {
        str = "DENIED";
      }
    } else {
      str = foreground ? "GRANTED_BACKGROUND" : "DENIED";
    }

    JSObject ret = new JSObject();
    ret.put("status", "ok");
    ret.put("messageAs", str);
    call.resolve(ret);
  }

  public void requestPermissions(final JSONArray args, final PluginCall call) throws JSONException {
    final boolean background = args.getBoolean(0);

    if (background && Build.VERSION.SDK_INT >= 29) {
      super.pluginRequestPermissions(
          new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_BACKGROUND_LOCATION"}, 0);
    } else {
      super.pluginRequestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 0);
    }

    JSObject ret = new JSObject();
    ret.put("status", "ok");
    call.resolve(ret);
  }

  public void handleNotification(final JSONArray args, final PluginCall call) throws JSONException {
    try {
      JSONObject pushData = args.getJSONObject(0);
      Type dataType = new TypeToken<Map<String, String>>() {
      }.getType();

      Map<String, String> data = gson.fromJson(pushData.toString(), dataType);

      FlyBuyCore.onMessageReceived(data, sdkError -> {
        if (sdkError != null) {
//                    Timber.e(sdkError.userError());
        }
        return null;
      });
    } catch (JSONException ex) {
//            Timber.e(ex);
    }
  }

  public void onEvents(final JSONArray args, final PluginCall call) throws JSONException {
    CapFlyBuyPlugin.eventsCallbackContext = call;

    int orderId;
    try {
      orderId = args.getInt(0);
    } catch (JSONException ex) {
//            Timber.e(ex);
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", ex.getMessage());
      call.resolve(ret);
      return;
    }

    this.getActivity().runOnUiThread(() -> {
      if (!observedIds.contains(orderId)) {
        FlyBuyCore.orders.getOrder(orderId).observeForever(orderObserver);
        observedIds.add(orderId);
      }
    });
  }

  public void offEvents(final JSONArray args, final PluginCall call) throws JSONException {
    CapFlyBuyPlugin.eventsCallbackContext = null;
    removeObservers();
  }

  private void event(JSONArray args, PluginCall call) {
    int orderId;
    String stateStr = null;

    try {
      orderId = args.getInt(0);
      stateStr = args.getString(1);
    } catch (JSONException ex) {
//            Timber.e(ex);
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", ex.getMessage());
      call.resolve(ret);
      return;
    }

    String[] strings = stateStr.toLowerCase().split("\\.");
    if (strings.length != 2) {
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", "State passed to function is not of the form <Entity>.<State>, i.e. customer.waiting");
      call.resolve(ret);
    }
    switch (strings[0]) {
      case "customer":
        String customerState = CustomerState.CREATED;
        if (strings.length > 1) {
          customerState = getCustomerState(strings[1]);
        }

        FlyBuyCore.orders.updateCustomerState(orderId, customerState, (order, error) -> {
          sendResult(order, error, call);
          return null;
        });
        break;
      case "order":
        String orderState = OrderState.CREATED;
        if (strings.length > 1) {
          orderState = getOrderState(strings[1]);
        }

        FlyBuyCore.orders.updateState(orderId, orderState, (order, error) -> {
          sendResult(order, error, call);
          return null;
        });
        break;
    }
  }

  private String getOrderState(String stateStr) {

    if (TextUtils.isEmpty(stateStr)) {
      return "Unknown";
    }

    switch (stateStr) {
      case "created":
        return OrderState.CREATED;
      case "ready":
        return OrderState.READY;
      case "delayed":
        return OrderState.DELAYED;
      case "cancelled":
        return OrderState.CANCELLED;
      case "completed":
        return OrderState.COMPLETED;
      case "gone":
        return OrderState.GONE;
    }
    return "Unknown";
  }

  private String getCustomerState(String stateStr) {
    if (TextUtils.isEmpty(stateStr)) {
      return CustomerState.CREATED;
    }

    switch (stateStr) {
      case "created":
        return CustomerState.CREATED;
      case "en_route":
        return CustomerState.EN_ROUTE;
      case "waiting":
        return CustomerState.WAITING;
      case "nearby":
        return CustomerState.NEARBY;
      case "arrived":
        return CustomerState.ARRIVED;
      case "completed":
        return CustomerState.COMPLETED;
    }
    return "Unknown";
  }

}
