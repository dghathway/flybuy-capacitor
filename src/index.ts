import { registerPlugin } from '@capacitor/core';

import type { CapFlyBuyPlugin } from './definitions';

const CapFlyBuy = registerPlugin<CapFlyBuyPlugin>('CapFlyBuy');

export * from './definitions';
export { CapFlyBuy };
