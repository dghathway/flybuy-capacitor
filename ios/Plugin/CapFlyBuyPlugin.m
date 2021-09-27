#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(CapFlyBuyPlugin, "CapFlyBuy",
    CAP_PLUGIN_METHOD(echo, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(onCreate, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(onActivityStarted, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(onActivityStopped, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(onLocationPermissionChanged, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(fetchOrders, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(getAllSites, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(getSitesByQuery, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(getConfig, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(createCustomer, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(updateCustomer, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(getCurrentCustomer, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(updatePushToken, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(handleNotification, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(createOrder, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(claimOrder, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(getPermissionStatus, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(requestPermissions, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(updateState, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(onEvents, CAPPluginReturnPromise);
    CAP_PLUGIN_METHOD(offEvents, CAPPluginReturnPromise);
)
