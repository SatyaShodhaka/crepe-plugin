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

  async initializeGraphQuery(): Promise<{ success: boolean }> {
    console.warn('initializeGraphQuery is not available on the web.');
    return Promise.resolve({ success: false });
  }

  async updateSnapshot(): Promise<{ success: boolean }> {
    console.warn('updateSnapshot is not available on the web.');
    return Promise.resolve({ success: false });
  }

  async queryGraph(options: { pattern: string }): Promise<{ results: any[] }> {
    console.warn(`queryGraph is not available on the web. Pattern: ${options.pattern}`);
    return Promise.resolve({ results: [] });
  }

  async matchCollectorData(options: { collectorData: string }): Promise<{ matches: any[] }> {
    console.warn(`matchCollectorData is not available on the web. Collector data: ${options.collectorData}`);
    return Promise.resolve({ matches: [] });
  }
}

