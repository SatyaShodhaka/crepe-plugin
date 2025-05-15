# crepe-plugin

Implements crepe application logic

## Install

Install the required dependencies using `pnpm`:
```
pnpm install
```
Install the custom plugin as a package:

```
pnpm install "path to the crepe-plugin directory on your local machine"
```

Sync the plugin with android:

```
npx cap sync android
```

Open in android studio:

```
npx cap open android
```

```bash
npm install crepe-plugin
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`startAccessibilityService()`](#startaccessibilityservice)
* [`stopAccessibilityService()`](#stopaccessibilityservice)
* [`getAccessibilityData()`](#getaccessibilitydata)
* [`initializeGraphQuery()`](#initializegraphquery)
* [`updateSnapshot()`](#updatesnapshot)
* [`queryGraph(...)`](#querygraph)
* [`matchCollectorData(...)`](#matchcollectordata)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### startAccessibilityService()

```typescript
startAccessibilityService() => Promise<void>
```

--------------------


### stopAccessibilityService()

```typescript
stopAccessibilityService() => Promise<void>
```

--------------------


### getAccessibilityData()

```typescript
getAccessibilityData() => Promise<{ data: string; }>
```

**Returns:** <code>Promise&lt;{ data: string; }&gt;</code>

--------------------


### initializeGraphQuery()

```typescript
initializeGraphQuery() => Promise<{ success: boolean; }>
```

**Returns:** <code>Promise&lt;{ success: boolean; }&gt;</code>

--------------------


### updateSnapshot()

```typescript
updateSnapshot() => Promise<{ success: boolean; }>
```

**Returns:** <code>Promise&lt;{ success: boolean; }&gt;</code>

--------------------


### queryGraph(...)

```typescript
queryGraph(options: { pattern: string; }) => Promise<{ results: any[]; }>
```

| Param         | Type                              |
| ------------- | --------------------------------- |
| **`options`** | <code>{ pattern: string; }</code> |

**Returns:** <code>Promise&lt;{ results: any[]; }&gt;</code>

--------------------


### matchCollectorData(...)

```typescript
matchCollectorData(options: { collectorData: string; }) => Promise<{ matches: any[]; }>
```

| Param         | Type                                    |
| ------------- | --------------------------------------- |
| **`options`** | <code>{ collectorData: string; }</code> |

**Returns:** <code>Promise&lt;{ matches: any[]; }&gt;</code>

--------------------

</docgen-api>
