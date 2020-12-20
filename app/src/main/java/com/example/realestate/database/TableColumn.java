package com.example.realestate.database;

import android.provider.BaseColumns;

public interface TableColumn extends BaseColumns {
	static final String CREATED_AT = "created_at";
	static final String UPDATED_AT = "updated_at";
	static final String CREATED_BY = "created_by";
	static final String UPDATED_BY = "updated_by";
	static final String USER_ID	 = "user_id";

	static final String NAME = "name";
	static final String CODE = "code";
    static final String VALUE = "value";
	static final String DESCRIPTION = "description";
    static final String FIRST_NAME = "first_name";
    static final String LAST_NAME = "last_name";

	static final String EMAIL = "email";
	static final String WORK_EMAIL = "work_email";

	static final String HOME_PHONE = "home_phone";
	static final String WORK_PHONE = "work_phone";
	static final String MOBILE_PHONE = "mobile_phone";

	static final String TYPE = "type";
    static final String ITEM_TYPE = "item_type";
    static final String ITEM_ID = "item_id";
    static final String TAXONOMY_ID = "vocabulary_id";
    static  final String TAXONOMY = "taxonomy";
    static  final String VOCABULARY = "taxonomy";

    static final String ATTACHMENT_TYPE = "attachment_type";
    static final String PATH = "path";
    static final String MIME = "mime";

	static final String PARENT = "parent";
    static final String PARENT_ID = "parent_id";
	static final String PARENT_NAME = "parent_name";

	static final String PROPERTY_ID = "property_id";
	static final String LOCALITY_ID = "locality_id";
	static final String CATEGORY_ID = "category_id";
	static final String FACILITY_ID = "facility_id";
    static final String STATUS_ID = "status_id";

    static final String AREA_UNIT = "area_unit";
    static final String CURRENCY_UNIT = "currency_unit";
	
	static final String PRICE = "price";
	static final String PRICE_UNIT_ID = "price_unit_id";
	static final String PRICE_POSTFIX = "price_postfix";
    static final String MIN_PRICE = "min_price";
    static final String MAX_PRICE = "max_price";

	static final String BROKERAGE = "brokerage";
	static final String BROKERAGE_TYPE = "brokerage_type";

	static final String ADDRESS = "address";
	static final String LONGITUDE = "longitude";
	static final String LATITUDE = "latitude";
    static final String LOCATION = "location";
    static final String FROM_TIME = "from_time";
    static final String TO_TIME = "to_time";

	static final String BEDROOMS = "num_bedrooms";
	static final String BATHROOMS = "num_bathrooms";
	static final String BALCONIES = "num_balconies";
	static final String GARAGES = "num_garages";

	static final String AREA = "area";
	static final String AREA_UNIT_ID = "area_unit_id";
    static final String MIN_AREA = "min_area";
    static final String MAX_AREA = "max_area";
    static final String COVERED_AREA = "covered_area";
    static final String CARPET_AREA = "carpet_area";
    static final String BUILTUP_AREA = "builtup_area";
    static final String SUPER_BUILTUP_AREA = "super_builtup_area";

	static final String FLOOR_NUMBER = "floor_number";
	static final String TOTAL_FLOORS = "total_floors";
	static final String CONSTRUCTION_YEAR = "construction_year";
	static final String FURNISHING = "furnishing_state";
	static final String FACING = "facing_orientation";
	static final String ALLOTTED_PARKING = "parking_lots";

	static final String USER_NAME = "username";
	static final String PASSWORD = "password";
	static final String DOB = "dob";
	static final String GENDER = "gender";
	static final String LANGUAGE = "language";
	static final String STATUS = "status";
	
	static final String COMPANY = "company";
	static final String GROUP = "group";
	static final String NOTE = "note";
	static final String TITLE = "title";
	static final String DEPARTMENT = "department";
	static final String CUSTOMER_ID = "customer_id";
	static final String THUMBNAIL_PATH = "thumbnail_path";
	static final String COVER_PIC_PATH = "cover_pic_path";
	static final String PICTURE_PATH = "picture_path";
    static final String DATA = "data";

    static final String APPOINTMENT_ID = "appointment_id";
    static final String REMINDER_TIME = "reminder_time";

    interface ExtendedColumn {
        static final String REMINDERS = "reminders";
        static final String NAME_VALUE_PAIRS = "name_value_pairs";
        static final String STATUSES = "statuses";
        static final String PICTURES = "pictures";
        static final String AMENITIES = "amenities";
        static final String FACILITIES = "facilities";
        static final String TYPES = "types";
    }
}
