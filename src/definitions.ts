export interface CAPResult {
  status: 'ok' | 'error'
  messageAs?: any
}

export type Customer = {
  name: string
  carType: string
  carColor: string
  licensePlate: string
  phone: string
}

export type Order = {
  siteId: number
  partnerId: string
  customer: Customer
  pickupWindow?: any
}

export type ICapFlyBuy = {
  onCreate(appKey: string): Promise<CAPResult>
  onActivityStarted(): Promise<CAPResult>
  onActivityStopped(): Promise<CAPResult>
  onLocationPermissionChanged(): Promise<CAPResult>
  fetchOrders(): Promise<CAPResult>
  getAllSites(): Promise<CAPResult>
  getSitesByQuery(query: string): Promise<CAPResult>
  getConfig(): Promise<CAPResult>
  createCustomer(customer: Customer): Promise<CAPResult>
  updateCustomer(customer: Customer): Promise<CAPResult>
  getCurrentCustomer(): Promise<CAPResult>
  updatePushToken(token: string): Promise<CAPResult>
  handleNotification(notification: any): Promise<CAPResult>
  createOrder(order: Order): Promise<CAPResult>
  claimOrder(code: string): Promise<CAPResult>
  getPermissionStatus(): Promise<CAPResult>
  requestPermissions(permissions: any): Promise<CAPResult>
  updateState(orderId: string, status: string): Promise<CAPResult>
  onEvents(): Promise<CAPResult>
  offEvents(): Promise<CAPResult>
}
export interface CapFlyBuyPlugin {
  onCreate(options: { appKey: string[] }): Promise<CAPResult>
  onActivityStarted(): Promise<CAPResult>
  onActivityStopped(): Promise<CAPResult>
  onLocationPermissionChanged(): Promise<CAPResult>
  fetchOrders(): Promise<CAPResult>
  getAllSites(): Promise<CAPResult>
  getSitesByQuery(options: { query: string[] }): Promise<CAPResult>
  getConfig(): Promise<CAPResult>
  createCustomer(options: { customer: Customer[] }): Promise<CAPResult>
  updateCustomer(options: { customer: Customer[] }): Promise<CAPResult>
  getCurrentCustomer(): Promise<CAPResult>
  updatePushToken(options: { token: string[] }): Promise<CAPResult>
  handleNotification(options: { notification: any[] }): Promise<CAPResult>
  createOrder(options: { order: Order[] }): Promise<CAPResult>
  claimOrder(options: { code: string[] }): Promise<CAPResult>
  getPermissionStatus(): Promise<CAPResult>
  requestPermissions(options: { permissions: boolean[] }): Promise<CAPResult>
  updateState(options: { event: any[] }): Promise<CAPResult>
  onEvents(): Promise<CAPResult>
  offEvents(): Promise<CAPResult>
}
