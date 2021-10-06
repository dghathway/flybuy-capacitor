import Foundation
import Capacitor
import CoreLocation
import FlyBuy
import FlyBuyPickup

typealias PluginResultType = [AnyHashable : Any]

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(CapFlyBuyPlugin)
public class CapFlyBuyPlugin: CAPPlugin {
    var locationManager: CLLocationManager!
    var eventsCallBack: CAPPluginCall!
    
    private var eventsCallbackId: String? = nil {
        didSet {
            setRegisteredForNotifications(registered: (eventsCallbackId != nil))
        }
    }
    
    override public func load() {
        let apiKey = self.getConfigValue("flybuyIosApiKey")!
        print(apiKey)
        FlyBuy.Core.configure(["token": apiKey as Any])
        FlyBuyPickup.Manager.shared.configure()
        locationManager = CLLocationManager()
    }
    
    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getArray("value")?[0] as? String ?? ""
        call.resolve([
            "status": "ok",
            "messageAs": value
        ])
    }
    
    private func handleNotification(_ notification: Notification, eventName: String) {
        let userInfo = notification.userInfo
        let data: [AnyHashable: Any] = userInfo?.merging(["eventName": eventName]) { (current, _) in current } ?? [:]
        eventsCallBack.resolve([
            "status": "ok",
            "messageAs": data
        ])
    }
    
    private func setRegisteredForNotifications(registered: Bool) {
        if (registered) {
            NotificationCenter.default.addObserver(forName: .ordersUpdated, object: nil, queue: nil) { [weak self] (notification) in
                // Update multiple orders
                self?.handleNotification(notification, eventName: "OrdersUpdated")
            }
            NotificationCenter.default.addObserver(forName: .orderUpdated, object: nil, queue: nil) { [weak self] (notification) in
                // Update single order
                self?.handleNotification(notification, eventName: "OrderUpdated")
            }
            NotificationCenter.default.addObserver(forName: .ordersError, object: nil, queue: nil) { [weak self] (notification) in
                // Handle error
                self?.handleNotification(notification, eventName: "OrdersError")
            }
        } else {
            NotificationCenter.default.removeObserver(self, name: .ordersUpdated, object: nil)
            NotificationCenter.default.removeObserver(self, name: .orderUpdated, object: nil)
            NotificationCenter.default.removeObserver(self, name: .ordersError, object: nil)
        }
    }
    
    private func handlePluginResult(result: Array<DictionaryRepresentable>?, error: Error?, call: CAPPluginCall) -> Void {
        
        if let result = result {
            let resultArray = result.map { $0.toDictionary() }
            call.resolve([
                "status": "ok",
                "messageAs": resultArray
            ])
        } else if let error = error {
            call.resolve([
                "status": "error",
                "messageAs": error.localizedDescription
            ])
        }
    }
    
    private func handlePluginResult<T: DictionaryRepresentable>(result: T?, error: Error?, call: CAPPluginCall) -> Void {
        if let result = result {
            call.resolve([
                "status": "ok",
                "messageAs": result
            ])
        } else if let error = error {
            call.resolve([
                "status": "error",
                "messageAs": error.localizedDescription
            ])
        }
    }
    
    private func getSites(query: String?, call: CAPPluginCall) {
        FlyBuy.Core.sites.fetchAll(query: query) { self.handlePluginResult(result: $0, error: $1, call: call) }
    }
    
    @objc func fetchOrders(_ call: CAPPluginCall) {
        FlyBuy.Core.orders.fetch() { self.handlePluginResult(result: $0, error: $1, call: call) }
    }
    
    @objc func getSitesByQuery(_ call: CAPPluginCall) {
        guard let queryTerm = call.getArray("query")?[0] as? String else {
            return call.resolve([
                "status": "error",
                "messageAs": "No parameters passed to function"
            ])
        }
        getSites(query: queryTerm, call: call)
    }
    
    @objc func getAllSites(_ call: CAPPluginCall) {
        getSites(query: nil, call: call)
    }
    
    @objc func createCustomer(_ call: CAPPluginCall) {
        guard let customerData = call.getArray("customer")?[0] as? NSDictionary else {
            return call.resolve([
                "status": "error",
                "messageAs": "No parameters passed to function"
            ])
        }
        
        let customerInfo = CustomerInfo(name: customerData["name"] as! String,
                                        carType: customerData["carType"] as? String,
                                        carColor: customerData["carColor"] as? String,
                                        licensePlate: customerData["licensePlate"] as? String,
                                        phone: customerData["phone"] as? String)
        
        FlyBuy.Core.customer.create(customerInfo, termsOfService: true, ageVerification: true)  { self.handlePluginResult(result: $0, error: $1, call: call) }
    }
    
    @objc func updateCustomer(_ call: CAPPluginCall) {
        guard let customerData = call.getArray("customer")?[0] as? NSDictionary else {
            return call.resolve([
                "status": "error",
                "messageAs": "No parameters passed to function"
            ])
        }
        
        let customerInfo = CustomerInfo(name: customerData["name"] as! String,
                                        carType: customerData["carType"] as? String,
                                        carColor: customerData["carColor"] as? String,
                                        licensePlate: customerData["licensePlate"] as? String,
                                        phone: customerData["phone"] as? String)
        
        FlyBuy.Core.customer.update(customerInfo){ (customer, error) -> (Void) in
            self.handlePluginResult(result: customer, error: error, call: call)
        }
    }
    
    @objc func getCurrentCustomer(_ call: CAPPluginCall) {
        guard let customer = FlyBuy.Core.customer.current else {
            return call.resolve([
                "status": "ok",
                "messageAs": ""
            ])
        }
        return call.resolve([
            "status": "ok",
            "messageAs": customer
        ])
    }
    
    @objc func updatePushToken(_ call: CAPPluginCall) {
        guard let token = call.getArray("token")?[0] as? String else {
            return call.resolve([
                "status": "error",
                "messageAs": "No token passed to function"
            ])
        }
        
        print("\(#function): updating push token to \(token)")
        FlyBuy.Core.updatePushToken(token)
        return call.resolve([
            "status": "ok",
            "messageAs": ""
        ])
    }
    
    @objc func handleNotification(_ call: CAPPluginCall) {
        guard let userInfo = call.getArray("notification")?[0] as? [AnyHashable: Any] else {
            return call.resolve([
                "status": "error",
                "messageAs": "No push info data to function"
            ])
        }
        
        FlyBuy.Core.handleRemoteNotification(userInfo)
        return call.resolve([
            "status": "ok",
            "messageAs": ""
        ])
    }
    
    @objc func createOrder(_ call: CAPPluginCall) {
        guard let siteId = call.getArray("order")?[0] as? Int,
              let partnerId = call.getArray("order")?[1] as? String,
              let customerInfo = call.getArray("order")?[2] as? NSDictionary
        else {
            return call.resolve([
                "status": "error",
                "messageAs": "Incorrect parameter count or types passed to function"
            ])
        }
        
        let customer = CustomerInfo(fromInfo: customerInfo)
        
        var pickupWindow: PickupWindow?
        if let pickupWindowArg = call.getArray("order")?[3] as? NSDictionary,
           let startTimeString = pickupWindowArg["startTime"] as? String,
           let endTimeString = pickupWindowArg["endTime"] as? String {
            
            let dateFormatter = DateFormatter()
            dateFormatter.locale = .init(identifier: "en_US_POSIX")
            dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
            
            if let startDate = dateFormatter.date(from: startTimeString),
               let endDate = dateFormatter.date(from: endTimeString) {
                pickupWindow = PickupWindow(start: startDate, end: endDate)
            } else {
                print("Error parsing pickupwindow dates: startTimeString = \(startTimeString), endTimeString = \(endTimeString)")
            }
        }
        
        FlyBuy.Core.orders.create(siteID: siteId, partnerIdentifier: partnerId, customerInfo: customer, pickupWindow: pickupWindow) { (order, error) in
            if let error = error {
                return self.handlePluginResult(result: order, error: error, call: call)
            } else if let order = order {
                FlyBuy.Core.orders.fetch() { (orders, error) in
                    if let fetchedOrder = orders?.first(where: { $0.id == order.id }) {
                        self.handlePluginResult(result: fetchedOrder, error: error, call: call)
                    } else {
                        self.handlePluginResult(result: order, error: error, call: call)
                    }
                    
                }
            } else {
                call.resolve([
                    "status": "error",
                    "messageAs": "Error creating the order with Flybuy, no order or error returned"
                ])
            }
        }
    }
    
    @objc func claimOrder(_ call: CAPPluginCall) {
        guard let redemptionCode = call.getArray("code")?[0] as? String
        else {
            return call.resolve([
                "status": "error",
                "messageAs": "Incorrect parameter count or types passed to function"
            ])
        }
        
        let claimFetchedOrder: ((Order) -> Void) = { order in
            // Re-use the existing customer info since there should always be some in any scenario where Flybuy receives the order from us or the Olo webhook (based on testing)
            let customerInfo = CustomerInfo(name: order.customerName ?? "Unknown",
                                            carType: order.customerCarType,
                                            carColor: order.customerCarColor,
                                            licensePlate: order.customerLicensePlate)
            
            let doClaim: (() -> Void) = {
                FlyBuy.Core.orders.claim(withRedemptionCode: redemptionCode, customerInfo: customerInfo, pickupType: order.pickupType) { (claimedOrder, error) in
                    // On instructions from Flybuy team, do another fetch after claiming to make sure it's up to date.
                    FlyBuy.Core.orders.fetch() { (orders, error) in
                        let foundOrder = orders?.first(where: { $0.id == order.id })
                        self.handlePluginResult(result: foundOrder, error: error, call: call) }
                }
            }
            
            // If we haven't established a customer with the SDK yet, we have to do that before claiming an order. Only do this if `currentCustomer` isn't initialized yet.
            if (FlyBuy.Core.customer.current == nil) {
                FlyBuy.Core.customer.create(customerInfo, termsOfService: true, ageVerification: true)  { (customer, error) in
                    doClaim()
                }
            } else {
                doClaim()
            }
        }
        
        // This logic is hairy because the Flybuy SDK is quite tempormental about redemption of a code when it's already been redeemed or fetched.
        // Step 1: Fetch all orders the Flybuy SDK currently knows about to see if we can do a match ourselves.
        FlyBuy.Core.orders.fetch() { (orders, error) in
            if let orders = orders, let foundOrder = orders.first(where: { $0.redemptionCode == redemptionCode }) {
                // If we found an order in the SDK fetch() cache, claim it.
                claimFetchedOrder(foundOrder)
            } else {
                // If we don't have it in our cache, fetch it by it's code to "get it into our cache"
                FlyBuy.Core.orders.fetch(withRedemptionCode: redemptionCode) { (order, error) in
                    if let error = error {
                        return self.handlePluginResult(result: order, error: error, call: call)
                    } else if let order = order {
                        // If Flybuy returned it, claim it.
                        claimFetchedOrder(order)
                    } else {
                        return call.resolve([
                            "status": "error",
                            "messageAs": "No error or order returned by Flybuy for redemption code \(redemptionCode)"
                        ])
                    }
                }
            }
        }
    }
    
    @objc func getPermissionStatus(_ call: CAPPluginCall) {
        let status = CLLocationManager.authorizationStatus()
        
        var str = "UNKNOWN";
        switch (status) {
        case .denied:
            str = "DENIED";
            break;
        case .restricted:
            str = "DENIED";
            break;
        case .authorizedAlways:
            str = "GRANTED_BACKGROUND";
            break;
        case .authorizedWhenInUse:
            str = "GRANTED_FOREGROUND";
            break;
        case .notDetermined:
            str = "NOT_YET_ASKED"
            break;
        default:
            str = "UNKNOWN";
        }
        
        return call.resolve([
            "status": "ok",
            "messageAs": str
        ])
    }
    
    //    @objc override public func requestPermissions(_ call: CAPPluginCall) {
    //        var isBackground = false
    //        if let bgNumber = call.getArray("permissions")?[0] as? Int {
    //            isBackground = bgNumber == 1
    //        }
    //
    //        if isBackground {
    //            locationManager.requestAlwaysAuthorization()
    //        } else {
    //            locationManager.requestWhenInUseAuthorization()
    //        }
    //
    //        return call.resolve([
    //            "status": "ok",
    //            "messageAs": ""
    //        ])
    //    }
    
    @objc func updateState(_ call: CAPPluginCall) {
        let sendErrorResult: ((String) -> Void) = { errorMsg in
            call.resolve([
                "status": "error",
                "messageAs": errorMsg
            ])
        }
        
        guard let orderId = call.getArray("event")?[0] as? Int,
              let stateStr = call.getArray("event")?[1] as? String else {
            return sendErrorResult("Incorrect parameter count or types passed to function")
        }
        
        let states = stateStr.lowercased().split(separator: ".")
        if states.count != 2 {
            return sendErrorResult("State passed to function is not of the form <Entity>.<State>, i.e. customer.waiting")
        }
        
        let type = String(states[0])
        let state = String(states[1])
        
        print("\(#function): type=\(type), state=\(state)")
        
        switch type {
        case "customer":
            if (CustomerState(stateString: state)) {
                FlyBuy.Core.orders.updateCustomerState(orderID: orderId, customerState: state) { self.handlePluginResult(result: $0, error: $1, call: call) }
            } else {
                return sendErrorResult("Invalid customer state")
            }
        case "order":
            if (OrderState(stateString: state)) {
                FlyBuy.Core.orders.updateOrderState(orderID: orderId, state: state) {
                    self.handlePluginResult(result: $0, error: $1, call: call) }
            } else {
                return sendErrorResult("Invalid order state")
            }
        default:
            sendErrorResult("Unrecognized type/state: [\(type), \(state)]")
            break
        }
    }
    
    @objc func onEvents(_ call: CAPPluginCall) {
        eventsCallbackId = call.callbackId
        eventsCallBack = call
    }
    
    @objc func offEvents(_ call: CAPPluginCall) {
        eventsCallbackId = nil
        eventsCallBack = nil
    }
    
}

protocol DictionaryRepresentable {
    func toDictionary() -> PluginResultType
}

class ArgumentError: Error {}
// https://www.radiusnetworks.com/developers/flybuy/#/sdk-2.0/ios-migration-guide?id=convert-to-state-strings
func CustomerState(stateString: String) -> Bool {
    return ["created", "enroute", "en_route", "nearby", "arrived", "waiting", "completed"].firstIndex(of: stateString.lowercased()) != -1
}

func OrderState(stateString: String) -> Bool {
    return ["created", "ready", "delayed", "cancelled", "completed", "gone"].firstIndex(of: stateString.lowercased()) != -1
    
}


extension CustomerInfo: DictionaryRepresentable {
    convenience init(fromInfo: NSDictionary) {
        self.init(name: fromInfo["name"] as! String,
                  carType: fromInfo["carType"] as? String,
                  carColor: fromInfo["carColor"] as? String,
                  licensePlate: fromInfo["licensePlate"] as? String,
                  phone: fromInfo["phone"] as? String)
    }
    
    func toDictionary() -> PluginResultType {
        var result: PluginResultType = [:]
        result["name"] = self.name
        result["carType"] = self.carType
        result["carColor"] = self.carColor
        result["licensePlate"] = self.licensePlate
        result["phone"] = self.phone
        return result
    }
}

extension Customer: DictionaryRepresentable {
    func toDictionary() -> PluginResultType {
        var result: PluginResultType = [:]
        result["apiToken"] = self.token
        result["emailAddress"] = self.emailAddress
        
        let info = self.info.toDictionary()
        result.merge(info) { (lhs, rhs) -> Any in
            return lhs
        }
        result["registered"] = self.registered
        return result
    }
}

extension Site: DictionaryRepresentable {
    static func toDictionary(_ site: Site) -> PluginResultType {
        return site.toDictionary()
    }
    
    func toDictionary() -> PluginResultType {
        var result: PluginResultType = [:]
        result["id"] = self.id
        result["partnerIdentifier"] = self.partnerIdentifier
        result["name"] = self.name
        result["phone"] = self.phone
        result["streetAddress"] = self.streetAddress
        result["fullAddress"] = self.fullAddress
        result["locality"] = self.locality
        result["region"] = self.region
        result["country"] = self.country
        result["postalCode"] = self.postalCode
        result["longitude"] = self.longitude
        result["latitude"] = self.latitude
        result["instructions"] = self.instructions
        result["descriptionText"] = self.descriptionText
        result["coverPhotoURL"] = self.coverPhotoURL
        result["projectAccentColor"] = self.projectAccentColor
        result["projectAccentTextColor"] = self.projectAccentTextColor
        result["projectLogoURL"] = self.projectLogoURL
        return result
    }
}


extension Order: DictionaryRepresentable {
    func toDictionary() -> PluginResultType {
        let dateFormatter = ISO8601DateFormatter()
        var result: PluginResultType = [:]
        result["id"] = self.id
        result["state"] = String(describing: self.state).lowercased()
        result["customerState"] = String(describing: self.customerState).lowercased()
        result["partnerIdentifier"] = self.partnerIdentifier
        result["pickupWindow"] = self.pickupWindow?.toDictionary()
        result["pickupType"] = self.pickupType
        if let date = self.etaAt {
            result["etaAt"] = dateFormatter.string(from: date)
        }
        if let date = self.redeemedAt {
            result["redeemedAt"] = dateFormatter.string(from: date)
        }
        result["customerRating"] = self.customerRating
        result["customerComment"] = self.customerComment
        result["siteID"] = self.siteID
        result["siteName"] = self.siteName
        result["sitePhone"] = self.sitePhone
        result["siteFullAddress"] = self.siteFullAddress
        result["siteLongitude"] = self.siteLongitude
        result["siteLatitude"] = self.siteLatitude
        result["siteInstructions"] = self.siteInstructions
        result["siteDescription"] = self.siteDescription
        result["siteCoverPhotoURL"] = self.siteCoverPhotoURL
        result["customerName"] = self.customerName
        result["customerCarType"] = self.customerCarType
        result["customerCarColor"] = self.customerCarColor
        result["customerLicensePlate"] = self.customerLicensePlate
        return result
    }
}

extension PickupWindow: DictionaryRepresentable {
    func toDictionary() -> PluginResultType {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = .init(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        return [
            "start": dateFormatter.string(from: self.start),
            "end": dateFormatter.string(from: self.end)
        ]
    }
}
