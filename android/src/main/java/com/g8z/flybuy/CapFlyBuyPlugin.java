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

  @PluginMethod
  public void onCreate(PluginCall call) throws JSONException {
    final String apiKey = call.getArray("appKey").getString(0);
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

  @PluginMethod
  public void onActivityStarted(PluginCall call) {
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

  @PluginMethod
  public void onActivityStopped(PluginCall call) {
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

  @PluginMethod
  public void onLocationPermissionChanged(PluginCall call) {
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      public void run() {
        JSObject ret = new JSObject();
        ret.put("status", "ok");
        call.resolve(ret);
      }
    });
  }

  @PluginMethod
  public void fetchOrders(PluginCall call) {
    FlyBuyCore.orders.fetch((orders, error) -> {
      sendResult(orders, error, call);
      return null;
    });
  }

  @PluginMethod
  public void getAllSites(PluginCall call) throws JSONException {
    String query = call.getArray("query").getString(0);

    FlyBuyCore.sites.fetchAll(query, (sites, error) -> {
      sendResult(sites, error, call);
      return null;
    });
  }

  @PluginMethod
  public void getConfig(PluginCall call) {
    FlyBuyCore.config.fetch(true, (data, error) -> {
      sendResult(data, error, call);
      return null;
    });
  }

  @PluginMethod
  public void claimOrder(PluginCall call) {
    try {
      String redemptionCode = call.getArray("code").getString(0);

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

  private void doClaim(Order order, CustomerInfo customerInfo, PluginCall call) {
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

  @PluginMethod
  public void createCustomer(PluginCall call) {
    final CustomerInfo customerInfo;
    // Build customer object from json
    try {
      JSONObject customerJSON = call.getArray("customer").getJSONObject(0);

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

  @PluginMethod
  public void createOrder(PluginCall call) throws JSONException {
    CustomerInfo customerInfo = null;
    int siteId = 0;
    String partnerId = null;
    PickupWindow pickupWindow = null;

    try {
      JSONObject order = call.getArray("order").getJSONObject(0);
      siteId = order.getInt("siteId");
      partnerId = order.getString("partnerId");
      JSONObject customerJSON = order.getJSONObject("customer");
      JSONObject pickupWindowJSON = null;
      if (order.has("pickupWindow")) {
        pickupWindowJSON = order.getJSONObject("pickupWindow");
      }
      customerInfo = new CustomerInfo(customerJSON.getString("name"), customerJSON.getString("phone"),
          customerJSON.getString("carType"), customerJSON.getString("carColor"),
          customerJSON.getString("licensePlate"));

      if (pickupWindowJSON != null) {
        Instant startInstant, endInstant;
        if (pickupWindowJSON.has("startTime")) {
          startInstant = Instant.parse(pickupWindowJSON.getString("startTime"));

          if (pickupWindowJSON.has("endTime")) {
            endInstant = Instant.parse(pickupWindowJSON.getString("endTime"));
          } else {
            endInstant = startInstant;
          }
          pickupWindow = new PickupWindow(startInstant, endInstant);
        }
      }

    } catch (JSONException ex) {
//      Timber.e(ex);
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", ex.getMessage());
      call.resolve(ret);
      return;
    }

    FlyBuyCore.orders.create(siteId, partnerId, customerInfo, pickupWindow, null, null, (createdOrder, error) -> {

      PickupManager.Companion.getInstance(null).onLocationPermissionChanged();

      FlyBuyCore.orders.fetch((orders, fetchOrdersError) -> {
        Order resultOrder = createdOrder;
        if (orders != null) {
          for (Order fetchedOrder : orders) {
            if (fetchedOrder.getId() == createdOrder.getId()) {
              resultOrder = fetchedOrder;
            }
          }
        }
        sendResult(resultOrder, fetchOrdersError, call);
        return null;
      });
      return null;
    });
  }

  private void updatePushToken(PluginCall call) {
    String token;
    try {
      token = call.getArray("token").getString(0);
    } catch (JSONException jsonException) {
//      Timber.e(jsonException);
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", jsonException.getMessage());
      call.resolve(ret);
      return;
    }

    FlyBuyCore.onNewPushToken(token);
  }

  @PluginMethod
  public void getCurrentCustomer(PluginCall call) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        Customer currentCustomer = FlyBuyCore.customer.getCurrent();
        sendResult(currentCustomer, null, call);
      }
    });
    thread.start();
  }

  @PluginMethod
  public void updateCustomer(PluginCall call) {
    final CustomerInfo customerInfo;
    try {
      JSONObject customerJSON = call.getArray("customer").getJSONObject(0);

      customerInfo = new CustomerInfo(customerJSON.getString("name"), customerJSON.getString("phone"),
          customerJSON.getString("carType"), customerJSON.getString("carColor"),
          customerJSON.getString("licensePlate"));

    } catch (JSONException exception) {
//      Timber.e(exception, "Error parsing customer JSON");
      JSObject ret = new JSObject();
      ret.put("status", "error");
      ret.put("messageAs", exception.getMessage());
      call.resolve(ret);
      return;
    }
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        FlyBuyCore.customer.update(customerInfo, (customer, sdkError) -> {
          sendResult(customer, sdkError, call);
          return null;
        });
      }
    });
    thread.start();
  }
  
  @PluginMethod
  public void getPermissionsStatus(PluginCall call) throws JSONException {
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

  @PluginMethod
  public void requestPermissions(PluginCall call) {
    boolean background = false;
    try {
      background = call.getArray("permissions").getBoolean(0);
    } catch (JSONException e) {
      e.printStackTrace();
    }

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

  @PluginMethod
  public void handleNotification(PluginCall call) {
    try {
      JSONObject pushData = call.getArray("notification").getJSONObject(0);
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

  @PluginMethod
  public void onEvents(PluginCall call) {
    CapFlyBuyPlugin.eventsCallbackContext = call;
    int orderId;
    orderId = call.getInt("0");
    this.getActivity().runOnUiThread(() -> {
      if (!observedIds.contains(orderId)) {
        FlyBuyCore.orders.getOrder(orderId).observeForever(orderObserver);
        observedIds.add(orderId);
      }
    });
  }

  @PluginMethod
  public void offEvents(PluginCall call) {
    CapFlyBuyPlugin.eventsCallbackContext = null;
    removeObservers();
  }

  private void event(PluginCall call) {
    int orderId;
    String stateStr = null;

    try {
      orderId = call.getArray("event").getInt(0);
      stateStr = call.getArray("event").getString(1);
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
