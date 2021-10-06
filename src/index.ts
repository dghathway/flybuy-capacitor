import { registerPlugin } from '@capacitor/core';

import type { CapFlyBuyPlugin, CAPResult } from './definitions';

const CapFlyBuyHandler = registerPlugin<CapFlyBuyPlugin>('CapFlyBuy');

type ICapFlyBuy = {
  // echo(value: string): Promise<CAPResult>
  onCreate(appKey: string): Promise<CAPResult>
  onActivityStarted(): Promise<CAPResult>
  onActivityStopped(): Promise<CAPResult>
  onLocationPermissionChanged(): Promise<CAPResult>
  fetchOrders(): Promise<CAPResult>
  getAllSites(): Promise<CAPResult>
  getSitesByQuery(query: string): Promise<CAPResult>
  getConfig(): Promise<CAPResult>
  createCustomer(customer: any): Promise<CAPResult>
  updateCustomer(customer: any): Promise<CAPResult>
  getCurrentCustomer(): Promise<CAPResult>
  updatePushToken(token: string): Promise<CAPResult>
  handleNotification(notification: any): Promise<CAPResult>
  createOrder(order: any): Promise<CAPResult>
  claimOrder(code: string): Promise<CAPResult>
  getPermissionStatus(): Promise<CAPResult>
  requestPermissions(permissions: any): Promise<CAPResult>
  updateState(orderId: string, status: string): Promise<CAPResult>
  onEvents(): Promise<CAPResult>
  offEvents(): Promise<CAPResult>
}

const CapFlyBuy: ICapFlyBuy = {
  // echo: (value: string) => {
  //   return CapFlyBuyHandler.echo({ value: [value] })
  // },
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
  createCustomer: (customer: any) => {
    return CapFlyBuyHandler.createCustomer({ customer: [customer] })
  },
  updateCustomer: (customer: any) => {
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
  createOrder: (order: any) => {
    return CapFlyBuyHandler.createOrder({ order: [order] })
  },
  claimOrder: (code: string) => {
    return CapFlyBuyHandler.claimOrder({ code: [code] })
  },
  getPermissionStatus: () => {
    return CapFlyBuyHandler.getPermissionStatus()
  },
  requestPermissions: (permission: any) => {
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
