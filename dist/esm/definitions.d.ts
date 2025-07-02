export interface StepData {
    steps: number;
}
export interface PedometerPlugin {
    start(): Promise<void>;
    stop(): Promise<void>;
    checkPermission(): Promise<{
        granted: boolean;
    }>;
    requestPermission(): Promise<{
        granted: boolean;
    }>;
    startBackground(): Promise<void>;
    stopBackground(): Promise<void>;
    getStoredSteps(): Promise<StepData>;
    getStepsBetween(options: {
        start: number;
        end: number;
    }): Promise<StepData>;
    addListener(eventName: 'stepUpdate', listenerFunc: (data: StepData) => void): void;
}
