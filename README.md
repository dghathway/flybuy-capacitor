# capacitor-flybuy-plugin

Flybuy plugin for capacitor

## Install

```bash
npm install capacitor-flybuy-plugin
npx cap sync
```

## API

<docgen-index>

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

### onCreate(...)

```typescript
onCreate(options: { appKey: string[]; }) => any
```

| Param         | Type                         |
| ------------- | ---------------------------- |
| **`options`** | <code>{ appKey: {}; }</code> |

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
getSitesByQuery(options: { query: string[]; }) => any
```

| Param         | Type                        |
| ------------- | --------------------------- |
| **`options`** | <code>{ query: {}; }</code> |

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
createCustomer(options: { customer: any[]; }) => any
```

| Param         | Type                           |
| ------------- | ------------------------------ |
| **`options`** | <code>{ customer: {}; }</code> |

**Returns:** <code>any</code>

--------------------


### updateCustomer(...)

```typescript
updateCustomer(options: { customer: any[]; }) => any
```

| Param         | Type                           |
| ------------- | ------------------------------ |
| **`options`** | <code>{ customer: {}; }</code> |

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
updatePushToken(options: { token: string[]; }) => any
```

| Param         | Type                        |
| ------------- | --------------------------- |
| **`options`** | <code>{ token: {}; }</code> |

**Returns:** <code>any</code>

--------------------


### handleNotification(...)

```typescript
handleNotification(options: { notification: any[]; }) => any
```

| Param         | Type                               |
| ------------- | ---------------------------------- |
| **`options`** | <code>{ notification: {}; }</code> |

**Returns:** <code>any</code>

--------------------


### createOrder(...)

```typescript
createOrder(options: { order: any[]; }) => any
```

| Param         | Type                        |
| ------------- | --------------------------- |
| **`options`** | <code>{ order: {}; }</code> |

**Returns:** <code>any</code>

--------------------


### claimOrder(...)

```typescript
claimOrder(options: { code: string[]; }) => any
```

| Param         | Type                       |
| ------------- | -------------------------- |
| **`options`** | <code>{ code: {}; }</code> |

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
requestPermissions(options: { permissions: any[]; }) => any
```

| Param         | Type                              |
| ------------- | --------------------------------- |
| **`options`** | <code>{ permissions: {}; }</code> |

**Returns:** <code>any</code>

--------------------


### updateState(...)

```typescript
updateState(options: { event: any[]; }) => any
```

| Param         | Type                        |
| ------------- | --------------------------- |
| **`options`** | <code>{ event: {}; }</code> |

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
