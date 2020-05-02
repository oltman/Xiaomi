/**
 *  Xiaomi Aqara Vibration Sensor
 *  Version 0.93
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Original device handler code by a4refillpad, adapted for use with Aqara model by bspranger
 *  Additional contributions to code by alecm, alixjg, bspranger, gn0st1c, foz333, jmagnuson, rinkek, ronvandegraaf, snalee, tmleafs, twonk, & veeceeoh 
 * 
 *  Known issues:
 *  Xiaomi sensors do not seem to respond to refresh requests
 *  Inconsistent rendering of user interface text/graphics between iOS and Android devices - This is due to SmartThings, not this device handler
 *  Pairing Xiaomi sensors can be difficult as they were not designed to use with a SmartThings hub. See 
 *
 */

metadata {
	definition (name: "Xiaomi Vibration Sensor RLO", namespace: "roltman", author: "roltman") {
        capability "Configuration"
        capability "Battery"
		capability "Motion Sensor"
		capability "Shock Sensor"
		capability "Sleep Sensor"
		capability "Three Axis"
        capability "Door Control"
        capability "Contact Sensor"

		
        attribute "lastCheckin", "String"
        attribute "lastCheckinDate", "String"
        attribute "lastMotion", "String"
        attribute "batteryRuntime", "String"
		attribute "Angle_X", "number"
		attribute "Angle_Y", "number"
		attribute "Angle_Z", "number"
		attribute "motionType", "String"
		
		
        fingerprint profileId: "0104", deviceId: "5F02", inClusters: " 0000, 0003, 0019, 0101", outClusters: "0000, 0004, 0003, 0005, 0019, 0101", manufacturer: "LUMI", model: "lumi.weather", deviceJoinName: "Xiaomi Aqara Vibration Sensor"
	    //fingerprint endpointId: "01", profileId: "0104", deviceId: "0107", inClusters: "0000,FFFF,0406,0400,0500,0001,0003", outClusters: "0000,0019", manufacturer: "LUMI", model: "lumi.sensor_motion.aq2", deviceJoinName: "Xiaomi Aqara Motion Sensor"
        //fingerprint profileId: "0104", deviceId: "0104", inClusters: "0000, 0400, 0406, FFFF", outClusters: "0000, 0019", manufacturer: "LUMI", model: "lumi.sensor_motion", deviceJoinName: "Xiaomi Aqara Motion Sensor"

        command "resetBatteryRuntime"
        command "stopMotion"
		command "setOpen"
		command "setClose"
    }

    simulator {
    }

    tiles(scale: 2) {
        multiAttributeTile(name:"motion", type:"generic", width: 6, height: 4) {
            tileAttribute ("device.motion", key: "PRIMARY_CONTROL") {
                attributeState "active", label:'Motion', icon:"st.motion.motion.active", backgroundColor:"#00a0dc"
                attributeState "inactive", label:'No Motion', icon:"st.motion.motion.inactive", backgroundColor:"#ffffff"
            }
            tileAttribute("device.lastMotion", key: "SECONDARY_CONTROL") {
                attributeState("default", label:'Last Motion: ${currentValue}')
            }
		}
        
        valueTile("door", "device.doorcontrol", decoration: "flat", inactiveLabel: false, width: 4, height: 2) {
        	state "default", label:'${currentValue}'}
        valueTile("contact", "device.contact", decoration: "flat", inactiveLabel: false, width: 2, height: 1) {
        	state "default", label:'${currentValue}'}
            
        valueTile("battery", "device.battery", decoration: "flat", inactiveLabel: false, width: 2, height: 2) {
            state "battery", label:'${currentValue}%', unit:"%", icon:"https://raw.githubusercontent.com/bspranger/Xiaomi/master/images/XiaomiBattery.png",
            backgroundColors:[
                [value: 10, color: "#bc2323"],
                [value: 26, color: "#f1d801"],
                [value: 51, color: "#44b621"]
            ]
        }
        valueTile("spacer", "spacer", decoration: "flat", inactiveLabel: false, width: 1, height: 1) {
            state "default", label:''
        }
        valueTile("spacer2", "spacer", decoration: "flat", inactiveLabel: false, width: 2, height: 2) {
            state "default", label:''
        }
		
		valueTile("motionType", "device.motionType", decoration:"flat", inactiveLabel: false, width: 2, height: 2) {
            state "default", label:'last motion:\n ${currentValue}'}
		valueTile("tiltX", "device.Angle_X", decoration:"flat", inactiveLabel: false, width: 2, height: 2) {
            state "default", label:'${currentValue}°'}
		valueTile("tiltY", "device.Angle_Y", decoration:"flat", inactiveLabel: false, width: 2, height: 2) {
            state "default", label:'${currentValue}°'}
		valueTile("tiltZ", "device.Angle_Z", decoration:"flat", inactiveLabel: false, width: 2, height: 2) {
            state "default", label:'${currentValue}°'}

			
		
        standardTile("reset", "device.reset", inactiveLabel: false, decoration:"flat", width: 2, height: 2) {
            state "default", action:"stopMotion", label:'Reset Motion', icon:"st.motion.motion.active"
        }
        valueTile("lastcheckin", "device.lastCheckin", decoration:"flat", inactiveLabel: false, width: 4, height: 1) {
            state "default", label:'Last Event:\n ${currentValue}'
        }
        valueTile("batteryRuntime", "device.batteryRuntime", inactiveLabel: false, decoration:"flat", width: 4, height: 1) {
             state "batteryRuntime", label:'Battery Changed:\n ${currentValue}'
        }
		
		standardTile("setClose", "device.setClose", inactiveLabel: false, decoration:"flat", width: 2, height: 2) {
            state "default", action:"setClose", label:'set close pos'
        }
		standardTile("setOpen", "device.setOpen", inactiveLabel: false, decoration:"flat", width: 2, height: 2) {
            state "default", action:"setOpen", label:'set open pos'
        }
		
		main(["door"])
		details(["motion", "door", "battery","motionType", "reset", "spacer2", "tiltX", "tiltY", "tiltZ", "spacer","lastcheckin", "spacer", "setClose", "setOpen"])
	}

	preferences {
		//Reset to No Motion Config
		input description: "This setting only changes how long MOTION DETECTED is reported in SmartThings. The sensor hardware always remains blind to motion for 60 seconds after any activity.", type: "paragraph", element: "paragraph", title: "MOTION RESET"
		input "motionreset", "number", title: "", description: "Enter number of seconds (default = 60)", range: "1..7200"
		//Date & Time Config
		input description: "", type: "paragraph", element: "paragraph", title: "DATE & CLOCK"    
		input name: "dateformat", type: "enum", title: "Set Date Format\n US (MDY) - UK (DMY) - Other (YMD)", description: "Date Format", options:["US","UK","Other"]
		input name: "clockformat", type: "bool", title: "Use 24 hour clock?"
		//Battery Reset Config
		input description: "If you have installed a new battery, the toggle below will reset the Changed Battery date to help remember when it was changed.", type: "paragraph", element: "paragraph", title: "CHANGED BATTERY DATE RESET"
		input name: "battReset", type: "bool", title: "Battery Changed?"
		//Battery Voltage Offset
		input description: "Only change the settings below if you know what you're doing.", type: "paragraph", element: "paragraph", title: "ADVANCED SETTINGS"
		input name: "voltsmax", title: "Max Volts\nA battery is at 100% at __ volts\nRange 2.8 to 3.4", type: "decimal", range: "2.8..3.4", defaultValue: 3
		input name: "voltsmin", title: "Min Volts\nA battery is at 0% (needs replacing) at __ volts\nRange 2.0 to 2.7", type: "decimal", range: "2..2.7", defaultValue: 2.5
        //test
	}	
}

// Parse incoming device messages to generate events
def parse(String description) {
    log.debug "${device.displayName} parsing: $description"

	// Determine current time and date in the user-selected date format and clock style
    def now = formatDate()    
    def nowDate = new Date(now).getTime()

	// Any report - motion, lux & Battery - results in a lastCheckin event and update to Last Event tile
	// However, only a non-parseable report results in lastCheckin being displayed in events log
    sendEvent(name: "lastCheckin", value: now, displayed: false)
    sendEvent(name: "lastCheckinDate", value: nowDate, displayed: false)

    Map map = [:]
	
	// Send message data to appropriate parsing function based on the type of report	
    if (description?.startsWith('illuminance:')) {
        map = parseIlluminance(description)
    }
    else if (description?.startsWith('read attr -')) {
        map = parseReportAttributeMessage(description)
    }
    // add back the below parseCatchAllMessage (3 lines) to display batter voltages in log...
    //else if (description?.startsWith('catchall:')) {
    //    map = parseCatchAllMessage(description)
    //}

    log.debug "${device.displayName} parse returned: $map"
    def result = map ? createEvent(map) : null

    return result
}

// Parse illuminance report
private Map parseIlluminance(String description) {
    def lux = ((description - "illuminance: ").trim()) as int

    def result = [
        name: 'illuminance',
        value: lux,
        unit: "lux",
        isStateChange: true,
        descriptionText : "${device.displayName} illuminance was ${lux} lux"
    ]
    return result
}
// Parse motion active report or model name message on reset button press
private Map parseReportAttributeMessage(String description) {
    def cluster = description.split(",").find {it.split(":")[0].trim() == "cluster"}?.split(":")[1].trim()
    def attrId = description.split(",").find {it.split(":")[0].trim() == "attrId"}?.split(":")[1].trim()
    def value = description.split(",").find {it.split(":")[0].trim() == "value"}?.split(":")[1].trim()

    Map resultMap = [:]
    def now = formatDate()

	// The sensor only sends a motion detected message so the reset to no motion is performed in code
    if (cluster == "0406" & value == "01") {
		log.debug "${device.displayName} detected motion"
		def seconds = motionreset ? motionreset : 120
		resultMap = [
			name: 'motion',
			value: 'active',
			descriptionText: "${device.displayName} detected motion"
		]
		sendEvent(name: "lastMotion", value: now, displayed: false)
		runIn(seconds, stopMotion)
	}
	else if (cluster == "0000" && attrId == "0005") {
        def modelName = ""
        // Parsing the model
        for (int i = 0; i < value.length(); i+=2) {
            def str = value.substring(i, i+2);
            def NextChar = (char)Integer.parseInt(str, 16);
            modelName = modelName + NextChar
        }
        log.debug "${device.displayName} reported: cluster: ${cluster}, attrId: ${attrId}, model:${modelName}"
    }
	else if (cluster=="0101" && attrId=="0055") {    //activity decode
		def seconds = motionreset ? motionreset : 120
		resultMap = [
			name: 'motion',
			value: 'active',
			descriptionText: "${device.displayName} detected motion"
			]
		sendEvent(name: "lastMotion", value: now, displayed: false)
		runIn(seconds, stopMotion)
		
		switch(value.substring(value.length()-4)) {  //look at rightmost four characters
			case "0001":
				sendEvent(name: "motionType", value: "vibration", displayed: false)
				break;
			case "0002":
				sendEvent(name: "motionType", value: "tilt", displayed: false)
				break;
			case "0003":
				sendEvent(name: "motionType", value: "drop", displayed: false)
				break;
		} 
			
	}
	else if (cluster == "0101" && attrId == "0508"){  //tilt value decode
		Integer X = Integer.parseInt(value.substring(8,12),16)
		Integer Y = Integer.parseInt(value.substring(4,8),16)
		Integer Z = Integer.parseInt(value.substring(0,4),16)
		if (X>=32768) X=X-65535
		if (Y>=32768) Y=Y-65535
		if (Z>=32768) Z=Z-65535
		float Psi = Math.round(Math.atan(X/Math.sqrt(Z*Z+Y*Y))*180/Math.PI)
		float Phi = Math.round(Math.atan(Y/Math.sqrt(X*X+Z*Z))*180/Math.PI)
		float Theta = Math.round(Math.atan(Z/Math.sqrt(X*X+Y*Y))*180/Math.PI)
        log.debug "${device.displayName} Accelerometer decode: raw=${value} X=${X} Y=${Y} Z=${Z}"
		log.debug "${device.displayName} Angle: Theta=${Theta}° Psi=${Psi}, Phi=${Phi}"
		sendEvent(name: "Angle_X", value: Psi, displayed: false)
		sendEvent(name: "Angle_Y", value: Phi, displayed: false)
		sendEvent(name: "Angle_Z", value: Theta, displayed: false)
		resultMap = [
        	name: 'threeAxis',
			value: [Psi, Phi, Theta]
            ]
		
        //detect closed position on angle change
        def float a=Float.parseFloat(state.X_closed)
        def float b=Float.parseFloat(state.Y_closed)
        def float c=Float.parseFloat(state.Z_closed)
        def float x=Float.parseFloat(state.X_open)
        def float y=Float.parseFloat(state.Y_open)
        def float z=Float.parseFloat(state.Z_open)
        def float e=10.0
        log.debug "${device.displayName} Psi=$Psi a=$a e=$e"
        if ((Psi<a+e)&&(Psi>a-e)&&(Phi<b+e)&&(Phi>b-e)&&(Theta<c+e)&&(Theta>c-e)){
           	sendEvent(name: "doorcontrol", value: "closed", displayed:false)
            sendEvent(name: "contact", value: "closed", displayed:false)

        }
        else if ((Psi<x+e)&&(Psi>x-e)&&(Phi<y+e)&&(Phi>y-e)&&(Theta<z+e)&&(Theta>z-e)){
           	sendEvent(name: "doorcontrol", value: "open", displayed:false)
            sendEvent(name: "contact", value: "open", displayed:false)
        }
        else sendEvent(name: "doorcontrol", value: "unknown", displayed: false)
            
            
            
    
    }
    return resultMap
}

// Check catchall for battery voltage data to pass to getBatteryResult for conversion to percentage report
private Map parseCatchAllMessage(String description) {
	Map resultMap = [:]
	def catchall = zigbee.parse(description)
	log.debug catchall

	if (catchall.clusterId == 0x0000) {
		def MsgLength = catchall.data.size()
		// Xiaomi CatchAll does not have identifiers, first UINT16 is Battery
		if ((catchall.data.get(0) == 0x01 || catchall.data.get(0) == 0x02) && (catchall.data.get(1) == 0xFF)) {
			for (int i = 4; i < (MsgLength-3); i++) {
				if (catchall.data.get(i) == 0x21) { // check the data ID and data type
					// next two bytes are the battery voltage
					resultMap = getBatteryResult((catchall.data.get(i+2)<<8) + catchall.data.get(i+1))
					break
				}
			}
		}
	}
	return resultMap
}

// Convert raw 4 digit integer voltage value into percentage based on minVolts/maxVolts range
private Map getBatteryResult(rawValue) {
    // raw voltage is normally supplied as a 4 digit integer that needs to be divided by 1000
    // but in the case the final zero is dropped then divide by 100 to get actual voltage value 
    def rawVolts = rawValue / 1000
	def minVolts
    def maxVolts

    if (voltsmin == null || voltsmin == "")
    	minVolts = 2.5
    else
   	minVolts = voltsmin
    
    if (voltsmax == null || voltsmax == "")
    	maxVolts = 3.0
    else
	maxVolts = voltsmax
    
    def pct = (rawVolts - minVolts) / (maxVolts - minVolts)
    def roundedPct = Math.min(100, Math.round(pct * 100))

    def result = [
        name: 'battery',
        value: roundedPct,
        unit: "%",
        isStateChange: true,
        descriptionText : "${device.displayName} Battery at ${roundedPct}% (${rawVolts} Volts)"
    ]
	// hide battery logging
	//log.debug "${device.displayName}: ${result}"
    return result
}


// If currently in 'active' motion detected state, stopMotion() resets to 'inactive' state and displays 'no motion'
def stopMotion() {
	if (device.currentState('motion')?.value == "active") {
		def seconds = motionreset ? motionreset : 120
		sendEvent(name:"motion", value:"inactive", isStateChange: true)
		log.debug "${device.displayName} reset to no motion after ${seconds} seconds"
	}
}

def setClose() {
	state.X_closed=device.currentState('Angle_X').value
	state.Y_closed=device.currentState('Angle_Y').value
	state.Z_closed=device.currentState('Angle_Z').value
	log.debug "${device.displayName} CLOSED defined as $state.X_closed,$state.Y_closed,$state.Z_closed"
}

def setOpen() {
	state.X_open=device.currentState('Angle_X').value
	state.Y_open=device.currentState('Angle_Y').value
	state.Z_open=device.currentState('Angle_Z').value
	log.debug "${device.displayName} OPEN defined as $state.X_open,$state.Y_open,$state.Z_open"
}


//Reset the date displayed in Battery Changed tile to current date
def resetBatteryRuntime(paired) {
	def now = formatDate(true)
	def newlyPaired = paired ? " for newly paired sensor" : ""
	sendEvent(name: "batteryRuntime", value: now)
	log.debug "${device.displayName}: Setting Battery Changed to current date${newlyPaired}"
}

// installed() runs just after a sensor is paired using the "Add a Thing" method in the SmartThings mobile app
def installed() {
	state.battery = 0
	if (!batteryRuntime) resetBatteryRuntime(true)
	checkIntervalEvent("installed")
}

// configure() runs after installed() when a sensor is paired
def configure() {
	log.debug "${device.displayName}: configuring"
		state.battery = 0
	if (!batteryRuntime) resetBatteryRuntime(true)
	checkIntervalEvent("configured")
	return
}

// updated() will run twice every time user presses save in preference settings page
def updated() {
		checkIntervalEvent("updated")
		if(battReset){
		resetBatteryRuntime()
		device.updateSetting("battReset", false)
	}
}

private checkIntervalEvent(text) {
    // Device wakes up every 1 hours, this interval allows us to miss one wakeup notification before marking offline
    log.debug "${device.displayName}: Configured health checkInterval when ${text}()"
    sendEvent(name: "checkInterval", value: 2 * 60 * 60 + 2 * 60, displayed: false, data: [protocol: "zigbee", hubHardwareId: device.hub.hardwareID])
}

def formatDate(batteryReset) {
    def correctedTimezone = ""
    def timeString = clockformat ? "HH:mm:ss" : "h:mm:ss aa"

	// If user's hub timezone is not set, display error messages in log and events log, and set timezone to GMT to avoid errors
    if (!(location.timeZone)) {
        correctedTimezone = TimeZone.getTimeZone("GMT")
        log.error "${device.displayName}: Time Zone not set, so GMT was used. Please set up your location in the SmartThings mobile app."
        sendEvent(name: "error", value: "", descriptionText: "ERROR: Time Zone not set, so GMT was used. Please set up your location in the SmartThings mobile app.")
    } 
    else {
        correctedTimezone = location.timeZone
    }
    if (dateformat == "US" || dateformat == "" || dateformat == null) {
        if (batteryReset)
            return new Date().format("MMM dd yyyy", correctedTimezone)
        else
            return new Date().format("EEE MMM dd yyyy ${timeString}", correctedTimezone)
    }
    else if (dateformat == "UK") {
        if (batteryReset)
            return new Date().format("dd MMM yyyy", correctedTimezone)
        else
            return new Date().format("EEE dd MMM yyyy ${timeString}", correctedTimezone)
        }
    else {
        if (batteryReset)
            return new Date().format("yyyy MMM dd", correctedTimezone)
        else
            return new Date().format("EEE yyyy MMM dd ${timeString}", correctedTimezone)
    }
}
