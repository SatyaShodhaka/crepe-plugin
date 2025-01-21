import { registerPlugin } from '@capacitor/core';

import type { CrepePlugin } from './definitions';

const Crepe = registerPlugin<CrepePlugin>('Crepe', {
  web: () => import('./web').then((m) => new m.CrepeWeb()),
});

export * from './definitions';
export { Crepe };
