# capacitor-flybuy-plugin

Flybuy plugin for capacitor

## Install

```bash
npm install capacitor-flybuy-plugin
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`onCreate(...)`](#oncreate)
* [`onActivityStarted()`](#onactivitystarted)
* [`onActivityStopped()`](#onactivitystopped)
* [`onLocationPermissionChanged()`](#onlocationpermissionchanged)
* [`fetchOrders()`](#fetchorders)
* [`getAllSites()`](#getallsites)
* [`getSitesByQuery(...)`](#getsitesbyquery)
* [`getConfig()`](#getconfig)
* [`createCustomer(...)`](#createcustomer)
* [`updateCustomer(...)`](#updatecustomer)
* [`getCurrentCustomer()`](#getcurrentcustomer)
* [`updatePushToken(...)`](#updatepushtoken)
* [`handleNotification(...)`](#handlenotification)
* [`createOrder(...)`](#createorder)
* [`claimOrder(...)`](#claimorder)
* [`getPermissionStatus()`](#getpermissionstatus)
* [`requestPermissions(...)`](#requestpermissions)
* [`updateState(...)`](#updatestate)
* [`onEvents()`](#onevents)
* [`offEvents()`](#offevents)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => any
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>any</code>

--------------------


### onCreate(...)

```typescript
onCreate(options: { appKey: string; }) => any
```

| Param         | Type                             |
| ------------- | -------------------------------- |
| **`options`** | <code>{ appKey: string; }</code> |

**Returns:** <code>any</code>

--------------------


### onActivityStarted()

```typescript
onActivityStarted() => any
```

**Returns:** <code>any</code>

--------------------


### onActivityStopped()

```typescript
onActivityStopped() => any
```

**Returns:** <code>any</code>

--------------------


### onLocationPermissionChanged()

```typescript
onLocationPermissionChanged() => any
```

**Returns:** <code>any</code>

--------------------


### fetchOrders()

```typescript
fetchOrders() => any
```

**Returns:** <code>any</code>

--------------------


### getAllSites()

```typescript
getAllSites() => any
```

**Returns:** <code>any</code>

--------------------


### getSitesByQuery(...)

```typescript
getSitesByQuery(options: { value: string; }) => any
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>any</code>

--------------------


### getConfig()

```typescript
getConfig() => any
```

**Returns:** <code>any</code>

--------------------


### createCustomer(...)

```typescript
createCustomer(options: { customer: any; }) => any
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ customer: any; }</code> |

**Returns:** <code>any</code>

--------------------


### updateCustomer(...)

```typescript
updateCustomer(options: { customer: any; }) => any
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ customer: any; }</code> |

**Returns:** <code>any</code>

--------------------


### getCurrentCustomer()

```typescript
getCurrentCustomer() => any
```

**Returns:** <code>any</code>

--------------------


### updatePushToken(...)

```typescript
updatePushToken(options: { pushToken: string; }) => any
```

| Param         | Type                                |
| ------------- | ----------------------------------- |
| **`options`** | <code>{ pushToken: string; }</code> |

**Returns:** <code>any</code>

--------------------


### handleNotification(...)

```typescript
handleNotification(options: { userInfo: any; }) => any
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ userInfo: any; }</code> |

**Returns:** <code>any</code>

--------------------


### createOrder(...)

```typescript
createOrder(options: { siteId: string; partnerId: string; customerInfo: any; pickupWindow: any; }) => any
```

| Param         | Type                                                                                      |
| ------------- | ----------------------------------------------------------------------------------------- |
| **`options`** | <code>{ siteId: string; partnerId: string; customerInfo: any; pickupWindow: any; }</code> |

**Returns:** <code>any</code>

--------------------


### claimOrder(...)

```typescript
claimOrder(options: { redemptionCode: string; }) => any
```

| Param         | Type                                     |
| ------------- | ---------------------------------------- |
| **`options`** | <code>{ redemptionCode: string; }</code> |

**Returns:** <code>any</code>

--------------------


### getPermissionStatus()

```typescript
getPermissionStatus() => any
```

**Returns:** <code>any</code>

--------------------


### requestPermissions(...)

```typescript
requestPermissions(options: { isForBackgroundTracking: any; }) => any
```

| Param         | Type                                           |
| ------------- | ---------------------------------------------- |
| **`options`** | <code>{ isForBackgroundTracking: any; }</code> |

**Returns:** <code>any</code>

--------------------


### updateState(...)

```typescript
updateState(options: { orderId: string; state: any; }) => any
```

| Param         | Type                                          |
| ------------- | --------------------------------------------- |
| **`options`** | <code>{ orderId: string; state: any; }</code> |

**Returns:** <code>any</code>

--------------------


### onEvents()

```typescript
onEvents() => any
```

**Returns:** <code>any</code>

--------------------


### offEvents()

```typescript
offEvents() => any
```

**Returns:** <code>any</code>

--------------------


### Interfaces


#### CAPResult

| Prop            | Type                         |
| --------------- | ---------------------------- |
| **`status`**    | <code>"error" \| "ok"</code> |
| **`messageAs`** | <code>any</code>             |

</docgen-api>
