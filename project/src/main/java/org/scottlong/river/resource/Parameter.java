package org.scottlong.river.resource;

public enum Parameter {
	
	AgencyCode("agency_cd", "Agency Code"),
	AltitudeAccuracy("alt_acy_va", "Altitude accuracy"),
	AltitudeDatum("alt_datum_cd", "Altitude datum"),
	AltitudeOfGageLandSurface("alt_va", "Altitude of Gage/land surface"),
	ContributingDrainageArea("contrib_drain_area_va", "Contributing drainage area"),
	CountryCode("country_cd", "Country code"),
	CountyCode("county_cd", "County code"),
	DataOtherGwFiles("gw_file_cd", "Data-other GW files"),
	DataReliabilityCode("reliability_cd", "Data reliability code"),
	DateOfFirstConstruction("construction_dt", "Date of first construction"),
	DateSiteEstablishedOrInventoried("inventory_dt", "Date site established or inventoried"),
	DateTime("datetime", "Date and Time"),
	DecimalLatitude("dec_lat_va", "Decimal latitude"),
	DecimalLatitudeLongitudeDatum("dec_coord_datum_cd", "Decimal Latitude-longitude datum"),
	DecimalLongitude("dec_long_va", "Decimal longitude"),
	DistrictCode("district_cd", "District code"),
	DmsLatitude("lat_va", "DMS latitude"),
	DmsLongitude("long_va", "DMS longitude"),
	DrainageArea("drain_area_va", "Drainage area"),
	DrainageBasinCode("basin_cd", "Drainage basin code"),
	FieldWaterLevelMeasurementsBeginDate("gw_begin_date", "Field water-level measurements begin date"),
	FieldWaterLevelMeasurementsCount("gw_count_nu", "Field water-level measurements count"),
	FieldWaterLevelMeasurementsEndDate("gw_end_date", "Field water-level measurements end date"),
	FlagsForInstrumentsAtSite("instruments_cd", "Flags for instruments at site"),
	FlagsForTheTypeOfDataCollected("data_types_cd", "Flags for the type of data collected"),
	HoleDepth("hole_depth_va", "Hole depth"),
	HydrologicUnitCode("huc_cd", "Hydrologic unit code"),
	LandNetLocationDescription("land_net_ds", "Land net location description"),
	LatitudeLongitudeAccuracy("coord_acy_cd", "Latitude-longitude accuracy"),
	LatitudeLongitudeDatum("coord_datum_cd", "Latitude-longitude datum"),
	LatitudeLongitudeMethod("coord_meth_cd", "Latitude-longitude method"),
	LocalAquiferCode("aqfr_cd", "Local aquifer code"),
	LocalAquiferTypeCode("aqfr_type_cd", "Local aquifer type code"),
	LocalStandardTimeFlag("local_time_fg", "Local standard time flag"),
	MeanGreenwichTimeOffset("tz_cd", "Mean Greenwich time offset"),
	MethodAltitudeDetermined("alt_meth_cd", "Method altitude determined"),
	NameOfLocationMap("map_nm", "Name of location map"),
	NationalAquiferCode("nat_aqfr_cd", "National aquifer code"),
	PeakStreamflowDataBeginDate("peak_begin_date", "Peak-streamflow data begin date"),
	PeakStreamflowDataCount("peak_count_nu", "Peak-streamflow data count"),
	PeakStreamflowDataEndDate("peak_end_date", "Peak-streamflow data end date"),
	ProjectNumber("project_no", "Project number"),
	RealTimeDataFlag("rt_bol", "Real-time data flag"),
	ScaleOfLocationMap("map_scale_fc", "Scale of location map"),
	SiteNumber("site_no", "Site Number"),
	SiteType("site_tp_cd", "Site type"),
	SiteVisitDataBeginDate("sv_begin_date", "Site-visit data begin date"),
	SiteVisitDataCount("sv_count_nu", "Site-visit data count"),
	SiteVisitDataEndDate("sv_end_date", "Site-visit data end date"),
	SourceOfDepthData("depth_src_cd", "Source of depth data"),
	StateCode("state_cd", "State code"),
	StationName("station_nm", "Station Name"),
	TimeZone("tz_cd", "Time Zone"),
	TopographicSettingCode("topo_cd", "Topographic setting code"),
	WaterQualityDataBeginDate("qw_begin_date", "Water-quality data begin date"),
	WaterQualityDataCount("qw_count_nu", "Water-quality data count"),
	WaterQualityDataEndDate("qw_end_date", "Water-quality data end date"),
	WellDepth("well_depth_va", "Well depth"),

	Celcius("00010", "Temperature, water, degrees Celsius"),
	Conductance("00095", "Specific conductance, water, unfiltered, microsiemens per centimeter at 25 degrees Celsius"),
	Discharge("00060", "Discharge, cubic feet per second"),
	Fahrenheit("00011", "Temperature, water, degrees Fahrenheit"),
	GageHeight("00065", "Gage height, feet"),
	Turbidity("63680", "Turbidity, water, unfiltered, monochrome near infra-red LED light, 780-900 nm, detection angle 90 +/ -2.5 degrees, formazin nephelometric units (FNU)");

	private String code;
	private String label;

	private Parameter(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

}
