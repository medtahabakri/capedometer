import { WebPlugin } from '@capacitor/core';

import type { PedometerPlugin } from './definitions';

export class PedometerWeb extends WebPlugin implements PedometerPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
  async queryData(options: {
    startDate: string,
    endDate: string
  }): Promise<{ value: number }> {
    console.log('Pedometer not available ine web', options);
    return { value: 0 };
  }
  async isStepCountingAvailable(): Promise<{ value: boolean }>{
    return { value: false };
  }
}
