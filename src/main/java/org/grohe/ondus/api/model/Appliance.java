package org.grohe.ondus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "applianceId")
public class Appliance {
    @JsonProperty("appliance_id")
    private String applianceId;
    @JsonProperty("installation_date")
    private String installationDate;
    private String name;
    @JsonProperty("serial_number")
    private String serialNumber;
    private Integer type;
    private String version;
    private String tdt;
    private Integer timezone;
    private Config config;
    private String role;
    @JsonProperty("registration_complete")
    private Boolean registrationComplete;
    @JsonIgnore
    private Room room = new Room();

    public Appliance(String applianceId, Room inRoom) {
        this.applianceId = applianceId;
        this.room = inRoom;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Config {
        private List<Threshold> thresholds = null;
        @JsonProperty("measurement_period")
        private Integer measurementPeriod;
        @JsonProperty("measurement_transmission_intervall")
        private Integer measurementTransmissionIntervall;
        @JsonProperty("measurement_transmission_intervall_offset")
        private Integer measurementTransmissionIntervallOffset;
        @JsonProperty("action_on_major_leakage")
        private Integer actionOnMajorLeakage;
        @JsonProperty("action_on_minor_leakage")
        private Integer actionOnMinorLeakage;
        @JsonProperty("action_on_micro_leakage")
        private Integer actionOnMicroLeakage;
        @JsonProperty("monitor_frost_alert")
        private Boolean monitorFrostAlert;
        @JsonProperty("monitor_lower_flow_limit")
        private Boolean monitorLowerFlowLimit;
        @JsonProperty("monitor_upper_flow_limit")
        private Boolean monitorUpperFlowLimit;
        @JsonProperty("monitor_lower_pressure_limit")
        private Boolean monitorLowerPressureLimit;
        @JsonProperty("monitor_upper_pressure_limit")
        private Boolean monitorUpperPressureLimit;
        @JsonProperty("monitor_lower_temperature_limit")
        private Boolean monitorLowerTemperatureLimit;
        @JsonProperty("monitor_upper_temperature_limit")
        private Boolean monitorUpperTemperatureLimit;
        @JsonProperty("monitor_major_leakage")
        private Boolean monitorMajorLeakage;
        @JsonProperty("monitor_minor_leakage")
        private Boolean monitorMinorLeakage;
        @JsonProperty("monitor_micro_leakage")
        private Boolean monitorMicroLeakage;
        @JsonProperty("monitor_system_error")
        private Boolean monitorSystemError;
        @JsonProperty("monitor_btw_0_1_and_0_8_leakage")
        private Boolean monitorBtw01And08Leakage;
        @JsonProperty("monitor_withdrawel_amount_limit_breach")
        private Boolean monitorWithdrawelAmountLimitBreach;
        @JsonProperty("detection_interval")
        private Integer detectionInterval;
        @JsonProperty("impulse_ignore")
        private Integer impulseIgnore;
        @JsonProperty("time_ignore")
        private Integer timeIgnore;
        @JsonProperty("pressure_tolerance_band")
        private Integer pressureToleranceBand;
        @JsonProperty("pressure_drop")
        private Integer pressureDrop;
        @JsonProperty("detection_time")
        private Integer detectionTime;
        @JsonProperty("action_on_btw_0_1_and_0_8_leakage")
        private Integer actionOnBtw01And08Leakage;
        @JsonProperty("action_on_withdrawel_amount_limit_breach")
        private Integer actionOnWithdrawelAmountLimitBreach;
        @JsonProperty("withdrawel_amount_limit")
        private Integer withdrawelAmountLimit;
        @JsonProperty("sprinkler_mode_start_time")
        private Integer sprinklerModeStartTime;
        @JsonProperty("sprinkler_mode_stop_time")
        private Integer sprinklerModeStopTime;
        @JsonProperty("sprinkler_mode_active_monday")
        private Boolean sprinklerModeActiveMonday;
        @JsonProperty("sprinkler_mode_active_tuesday")
        private Boolean sprinklerModeActiveTuesday;
        @JsonProperty("sprinkler_mode_active_wednesday")
        private Boolean sprinklerModeActiveWednesday;
        @JsonProperty("sprinkler_mode_active_thursday")
        private Boolean sprinklerModeActiveThursday;
        @JsonProperty("sprinkler_mode_active_friday")
        private Boolean sprinklerModeActiveFriday;
        @JsonProperty("sprinkler_mode_active_saturday")
        private Boolean sprinklerModeActiveSaturday;
        @JsonProperty("sprinkler_mode_active_sunday")
        private Boolean sprinklerModeActiveSunday;

        @Getter
        @Setter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Threshold {
            @JsonProperty("quantity")
            private String quantity;
            @JsonProperty("type")
            private String type;
            @JsonProperty("value")
            private Integer value;
            @JsonProperty("enabled")
            private Boolean enabled;
        }
    }
}
