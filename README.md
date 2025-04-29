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

</docgen-api>
