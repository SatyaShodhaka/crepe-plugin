import { registerPlugin } from '@capacitor/core';

export interface CrepePlugin {
    requestAccessibilityPermission(): Promise<void>;
    startAccessibilityService(): Promise<void>;
}

const Crepe = registerPlugin<CrepePlugin>('CrepePlugin');

export { Crepe };
