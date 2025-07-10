import Foundation
import Capacitor
import CoreMotion

@objc(PedometerPlugin)
public class PedometerPlugin: CAPPlugin, CAPBridgedPlugin {
    public var identifier: String = "PedometerPlugin"
    
    public var jsName: String = "Pedometer"
    
    public var pluginMethods: [CAPPluginMethod] =  [
      CAPPluginMethod(name: "start", returnType: CAPPluginReturnPromise),
      CAPPluginMethod(name: "stop", returnType: CAPPluginReturnPromise),
      CAPPluginMethod(name: "checkPermission", returnType: CAPPluginReturnPromise),
      CAPPluginMethod(name: "requestPermission", returnType: CAPPluginReturnPromise),
      CAPPluginMethod(name: "getStepsBetween", returnType: CAPPluginReturnPromise),
      CAPPluginMethod(name: "startBackground", returnType: CAPPluginReturnPromise),
      CAPPluginMethod(name: "stopBackground", returnType: CAPPluginReturnPromise)
    ]

  
    private let pedometer = CMPedometer()
    private var isObserving = false

    @objc func start(_ call: CAPPluginCall) {
        if CMPedometer.isStepCountingAvailable() {
            isObserving = true
            pedometer.startUpdates(from: Date()) { data, error in
                guard let data = data, error == nil else {
                    return
                }

                let ret: [String: Any] = [
                    "steps": data.numberOfSteps.intValue
                ]

                self.notifyListeners("stepUpdate", data: ret)
            }
            call.resolve()
        } else {
            call.reject("Step counting not available")
        }
    }

    @objc func stop(_ call: CAPPluginCall) {
        if isObserving {
            pedometer.stopUpdates()
            isObserving = false
        }
        call.resolve()
    }
    
    @objc func checkPermission(_ call: CAPPluginCall) {
        let available = CMPedometer.isStepCountingAvailable()
        let result = ["granted": available]
        call.resolve(result)
    }

    @objc func requestPermission(_ call: CAPPluginCall) {
        if CMPedometer.isStepCountingAvailable() {
            pedometer.queryPedometerData(from: Date(), to: Date()) { data, error in
                let granted = (error == nil)
                call.resolve(["granted": granted])
            }
        } else {
            call.resolve(["granted": false])
        }
    }

    @objc func getStepsBetween(_ call: CAPPluginCall) {
        guard let start = call.getDouble("start"),
              let end = call.getDouble("end") else {
            call.reject("Missing 'start' or 'end' timestamps")
            return
        }

        let startDate = Date(timeIntervalSince1970: start / 1000) // from ms to seconds
        let endDate = Date(timeIntervalSince1970: end / 1000)

        if CMPedometer.isStepCountingAvailable() {
            pedometer.queryPedometerData(from: startDate, to: endDate) { data, error in
                if let error = error {
                    call.reject("Error: \(error.localizedDescription)")
                    return
                }

                if let data = data {
                    var result = JSObject()
                    result["steps"] = data.numberOfSteps.intValue
                    call.resolve(result)
                } else {
                    call.reject("No pedometer data found")
                }
            }
        } else {
            call.reject("Step counting not available")
        }
    }

    @objc func startBackground(_ call: CAPPluginCall) {
        call.reject("Background step counting not available in IOS")
    }

    @objc func stopBackground(_ call: CAPPluginCall) {
        call.reject("Background step counting not available in IOS")
    }
}
