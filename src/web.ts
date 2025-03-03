import { WebPlugin } from '@capacitor/core';

import type { CrepePlugin } from './definitions';

export class CrepeWeb extends WebPlugin implements CrepePlugin {

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async startAccessibilityService(): Promise<void> {
    console.warn('startAccessibilityService is not available on the web.');
    return Promise.resolve();
  }

  async stopAccessibilityService(): Promise<void> {
    console.warn('stopAccessibilityService is not available on the web.');
    return Promise.resolve();
  }

  async getAccessibilityData(): Promise<{ data: string }> {
    console.warn('getAccessibilityData is not available on the web.');
    return Promise.resolve({ data: '' });
  }
}

