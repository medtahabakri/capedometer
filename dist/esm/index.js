import { registerPlugin } from '@capacitor/core';
const Pedometer = registerPlugin('Pedometer');
// const Pedometer = registerPlugin<PedometerPlugin>('Pedometer', {
//   web: () => import('./web').then((m) => new m.PedometerWeb()),
// });
export * from './definitions';
export { Pedometer };
//# sourceMappingURL=index.js.map