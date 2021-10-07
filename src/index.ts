import { registerPlugin } from '@capacitor/core';

import type {
  CapFlyBuyPlugin,
  ICapFlyBuy,
  Order,
  Customer
} from './definitions';

const CapFlyBuyHandler = registerPlugin<CapFlyBuyPlugin>('CapFlyBuy');

const CapFlyBuy: ICapFlyBuy = {
  onCreate: (appKey: string) => {
    return CapFlyBuyHandler.onCreate({ appKey: [appKey] })
  },
  onActivityStarted: () => {
    return CapFlyBuyHandler.onActivityStarted()
  },
  onActivityStopped: () => {
    return CapFlyBuyHandler.onActivityStopped()
  },
  onLocationPermissionChanged: () => {
    return CapFlyBuyHandler.onLocationPermissionChanged()
  },
  fetchOrders: () => {
    return CapFlyBuyHandler.fetchOrders()
  },
  getAllSites: () => {
    return CapFlyBuyHandler.getAllSites()
  },
  getSitesByQuery: (query: string) => {
    return CapFlyBuyHandler.getSitesByQuery({ query: [query] })
  },
  getConfig: () => {
    return CapFlyBuyHandler.getConfig()
  },
  createCustomer: (customer: Customer) => {
    return CapFlyBuyHandler.createCustomer({ customer: [customer] })
  },
  updateCustomer: (customer: Customer) => {
    return CapFlyBuyHandler.updateCustomer({ customer: [customer] })
  },
  getCurrentCustomer: () => {
    return CapFlyBuyHandler.getCurrentCustomer()
  },
  updatePushToken: (token: string) => {
    return CapFlyBuyHandler.updatePushToken({ token: [token] })
  },
  handleNotification: (notification: any) => {
    return CapFlyBuyHandler.handleNotification({ notification: [notification] })
  },
  createOrder: (order: Order) => {
    return CapFlyBuyHandler.createOrder({ order: [order] })
  },
  claimOrder: (code: string) => {
    return CapFlyBuyHandler.claimOrder({ code: [code] })
  },
  getPermissionStatus: () => {
    return CapFlyBuyHandler.getPermissionStatus()
  },
  requestPermissions: (permission: boolean) => {
    return CapFlyBuyHandler.requestPermissions({ permissions: [permission] })
  },
  updateState: (orderId: string, status: string) => {
    return CapFlyBuyHandler.updateState({ event: [orderId, status] })
  },
  onEvents: () => {
    return CapFlyBuyHandler.onEvents()
  },
  offEvents: () => {
    return CapFlyBuyHandler.offEvents()
  }
}

export * from './definitions';
export { CapFlyBuy };
