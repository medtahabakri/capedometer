export interface StepData {
  steps: number;
}

export interface PedometerPlugin {
  start(): Promise<void>;
  stop(): Promise<void>;
  checkPermission(): Promise<{ granted: boolean }>;
  requestPermission(): Promise<{ granted: boolean }>;

  // run in background (ANDROID)
  startBackground(): Promise<void>;
  stopBackground(): Promise<void>;
  getStoredSteps(): Promise<StepData>;

  // get steps between two timestamps (IOS)
  getStepsBetween(options: {
    start: number; // timestamp in ms
    end: number;
  }): Promise<StepData>;

  addListener(eventName: 'stepUpdate', listenerFunc: (data: StepData) => void): void;
}