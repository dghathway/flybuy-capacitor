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
    private let implementation = CapFlyBuy()
    var locationManager: CLLocationManager!
    
    override public func load() {
        let apiKey = self.getConfigValue("flybuyIosApiKey")!
        print(apiKey)
        FlyBuy.Core.configure(["token": apiKey as Any])
        //        FlyBuyPickup.Manager.shared.configure()
        locationManager = CLLocationManager()
    }
    
    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": value
        ])
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
            call.resolve([
                "status": "error",
                "messageAs": "No parameters passed to function"
            ])
            return
        }
        getSites(query: queryTerm, call: call)
    }
    
    @objc func getAllSites(_ call: CAPPluginCall) {
        getSites(query: nil, call: call)
    }
    
    @objc func createCustomer(_ call: CAPPluginCall) {
        guard let customerData = call.getArray("customer")?[0] as? NSDictionary else {
            call.resolve([
                "status": "error",
                "messageAs": "No parameters passed to function"
            ])
            return
        }
        
        let customerInfo = CustomerInfo(name: customerData["name"] as! String,
                                        carType: customerData["carType"] as? String,
                                        carColor: customerData["carColor"] as? String,
                                        licensePlate: customerData["licensePlate"] as? String,
                                        phone: customerData["phone"] as? String)
        
        FlyBuy.Core.customer.create(customerInfo, termsOfService: true, ageVerification: true)  { self.handlePluginResult(result: $0, error: $1, call: call) }
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
