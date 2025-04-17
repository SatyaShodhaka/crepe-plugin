# crepe-plugin

Implements crepe application logic

## Install

Install the required dependencies using `pnpm`:
```
pnpm install
```
Install the custom plugin as a package:

```
pnpm install /custom-capacitor-plugin/crepe-plugin
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

* [`requestAccessibilityPermission()`](#requestaccessibilitypermission)
* [`startAccessibilityService()`](#startaccessibilityservice)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### requestAccessibilityPermission()

```typescript
requestAccessibilityPermission() => Promise<void>
```

--------------------


### startAccessibilityService()

```typescript
startAccessibilityService() => Promise<void>
```

--------------------

</docgen-api>
