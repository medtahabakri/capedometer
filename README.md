# capedometer

Pedometer plugin for capacitor inspired by cordova-plugin-pedometer

## Install

```bash
npm install capedometer
npx cap sync
```

--------------------

## Configuration

### ANDROID

#### In AndroidManifest.xml

Inside <code> \<application /> </code> 

```XML
<service
    android:name="com.medtahabakri.plugins.capedometer.StepService"
    android:exported="false" 
/>
```

Inside <code> \<manifest /> </code>  add following permissions :

```XML
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_PHYSICAL_ACTIVITY" />
```

--------------------

### IOS

#### In Info.plist

Add the following descriptions :

```XML
<key>NSMotionUsageDescription</key>
<string>This app needs access to motion and fitness activity to count your steps.</string>
```

--------------------

## API

<docgen-index>

* [`start()`](#start)
* [`stop()`](#stop)
* [`checkPermission()`](#checkpermission)
* [`requestPermission()`](#requestpermission)
* [`startBackground()`](#startbackground)
* [`stopBackground()`](#stopbackground)
* [`getStoredSteps()`](#getstoredsteps)
* [`getStepsBetween(...)`](#getstepsbetween)
* [`addListener('stepUpdate', ...)`](#addlistenerstepupdate-)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### start()

```typescript
start() => Promise<void>
```

--------------------


### stop()

```typescript
stop() => Promise<void>
```

--------------------


### checkPermission()

```typescript
checkPermission() => Promise<{ granted: boolean; }>
```

**Returns:** <code>Promise&lt;{ granted: boolean; }&gt;</code>

--------------------


### requestPermission()

```typescript
requestPermission() => Promise<{ granted: boolean; }>
```

**Returns:** <code>Promise&lt;{ granted: boolean; }&gt;</code>

--------------------


### startBackground()

```typescript
startBackground() => Promise<void>
```

--------------------


### stopBackground()

```typescript
stopBackground() => Promise<void>
```

--------------------


### getStoredSteps()

```typescript
getStoredSteps() => Promise<StepData>
```

**Returns:** <code>Promise&lt;<a href="#stepdata">StepData</a>&gt;</code>

--------------------


### getStepsBetween(...)

```typescript
getStepsBetween(options: { start: number; end: number; }) => Promise<StepData>
```

| Param         | Type                                         |
| ------------- | -------------------------------------------- |
| **`options`** | <code>{ start: number; end: number; }</code> |

**Returns:** <code>Promise&lt;<a href="#stepdata">StepData</a>&gt;</code>

--------------------


### addListener('stepUpdate', ...)

```typescript
addListener(eventName: 'stepUpdate', listenerFunc: (data: StepData) => void) => void
```

| Param              | Type                                                             |
| ------------------ | ---------------------------------------------------------------- |
| **`eventName`**    | <code>'stepUpdate'</code>                                        |
| **`listenerFunc`** | <code>(data: <a href="#stepdata">StepData</a>) =&gt; void</code> |

--------------------


### Interfaces


#### StepData

| Prop        | Type                |
| ----------- | ------------------- |
| **`steps`** | <code>number</code> |

</docgen-api>
