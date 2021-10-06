export interface CAPResult {
  status: 'ok' | 'error'
  messageAs?: any
}

export interface CapFlyBuyPlugin {
  // echo(options: { value: string[] }): Promise<CAPResult>
  onCreate(options: { appKey: string[] }): Promise<CAPResult>
  onActivityStarted(): Promise<CAPResult>
  onActivityStopped(): Promise<CAPResult>
  onLocationPermissionChanged(): Promise<CAPResult>
  fetchOrders(): Promise<CAPResult>
  getAllSites(): Promise<CAPResult>
  getSitesByQuery(options: { query: string[] }): Promise<CAPResult>
  getConfig(): Promise<CAPResult>
  createCustomer(options: { customer: any[] }): Promise<CAPResult>
  updateCustomer(options: { customer: any[] }): Promise<CAPResult>
  getCurrentCustomer(): Promise<CAPResult>
  updatePushToken(options: { token: string[] }): Promise<CAPResult>
  handleNotification(options: { notification: any[] }): Promise<CAPResult>
  createOrder(options: { order: any[] }): Promise<CAPResult>
  claimOrder(options: { code: string[] }): Promise<CAPResult>
  getPermissionStatus(): Promise<CAPResult>
  requestPermissions(options: { permissions: any[] }): Promise<CAPResult>
  updateState(options: { event: any[] }): Promise<CAPResult>
  onEvents(): Promise<CAPResult>
  offEvents(): Promise<CAPResult>
}
