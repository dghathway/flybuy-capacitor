import { registerPlugin } from '@capacitor/core';
const CapFlyBuyHandler = registerPlugin('CapFlyBuy');
const CapFlyBuy = {
    onCreate: (appKey) => {
        return CapFlyBuyHandler.onCreate({ appKey: [appKey] });
    },
    onActivityStarted: () => {
        return CapFlyBuyHandler.onActivityStarted();
    },
    onActivityStopped: () => {
        return CapFlyBuyHandler.onActivityStopped();
    },
    onLocationPermissionChanged: () => {
        return CapFlyBuyHandler.onLocationPermissionChanged();
    },
    fetchOrders: () => {
        return CapFlyBuyHandler.fetchOrders();
    },
    getAllSites: () => {
        return CapFlyBuyHandler.getAllSites();
    },
    getSitesByQuery: (query) => {
        return CapFlyBuyHandler.getSitesByQuery({ query: [query] });
    },
    getConfig: () => {
        return CapFlyBuyHandler.getConfig();
    },
    createCustomer: (customer) => {
        return CapFlyBuyHandler.createCustomer({ customer: [customer] });
    },
    updateCustomer: (customer) => {
        return CapFlyBuyHandler.updateCustomer({ customer: [customer] });
    },
    getCurrentCustomer: () => {
        return CapFlyBuyHandler.getCurrentCustomer();
    },
    updatePushToken: (token) => {
        return CapFlyBuyHandler.updatePushToken({ token: [token] });
    },
    handleNotification: (notification) => {
        return CapFlyBuyHandler.handleNotification({ notification: [notification] });
    },
    createOrder: (order) => {
        return CapFlyBuyHandler.createOrder({ order: [order] });
    },
    claimOrder: (code) => {
        return CapFlyBuyHandler.claimOrder({ code: [code] });
    },
    getPermissionStatus: () => {
        return CapFlyBuyHandler.getPermissionStatus();
    },
    requestPermissions: (permission) => {
        return CapFlyBuyHandler.requestPermissions({ permissions: [permission] });
    },
    updateState: (orderId, status) => {
        return CapFlyBuyHandler.updateState({ event: [orderId, status] });
    },
    onEvents: () => {
        return CapFlyBuyHandler.onEvents();
    },
    offEvents: () => {
        return CapFlyBuyHandler.offEvents();
    }
};
export * from './definitions';
export { CapFlyBuy };
//# sourceMappingURL=index.js.map