import { WebPlugin } from '@capacitor/core';

import type { CrepePlugin } from './definitions';

export class CrepeWeb extends WebPlugin implements CrepePlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
