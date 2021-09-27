export interface CAPResult {
  status: 'ok' | 'error'
  messageAs?: any
}

export interface CapFlyBuyPlugin {
  echo(options: { value: string }): Promise<{ value: string }>
  onCreate(options: { appKey: string }): Promise<CAPResult>
  onActivityStarted(): Promise<CAPResult>
  onActivityStopped(): Promise<CAPResult>
  onLocationPermissionChanged(): Promise<CAPResult>
  fetchOrders(): Promise<CAPResult>
  getAllSites(): Promise<CAPResult>
  getSitesByQuery(options: { value: string }): Promise<CAPResult>
  getConfig(): Promise<CAPResult>
  createCustomer(options: { customer: any }): Promise<CAPResult>
  updateCustomer(options: { customer: any }): Promise<CAPResult>
  getCurrentCustomer(): Promise<CAPResult>
  updatePushToken(options: { pushToken: string }): Promise<CAPResult>
  handleNotification(options: { userInfo: any }): Promise<CAPResult>
  createOrder(options: { siteId: string, partnerId: string, customerInfo: any, pickupWindow: any }): Promise<CAPResult>
  claimOrder(options: { redemptionCode: string }): Promise<CAPResult>
  getPermissionStatus(): Promise<CAPResult>
  requestPermissions(options: { isForBackgroundTracking: any }): Promise<CAPResult>
  updateState(options: { orderId: string, state: any }): Promise<CAPResult>
  onEvents(): Promise<CAPResult>
  offEvents(): Promise<CAPResult>
}
