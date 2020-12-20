package com.example.realestate.provider;

import android.net.Uri;

import com.example.realestate.database.table.AppointmentTable;
import com.example.realestate.database.table.CustomerTable;
import com.example.realestate.database.table.ItemAttachmentTable;
import com.example.realestate.database.table.ItemAttributeTable;
import com.example.realestate.database.table.ItemVocabularyTable;
import com.example.realestate.database.table.LeadTable;
import com.example.realestate.database.table.PropertyTable;
import com.example.realestate.database.table.TaxonomyTable;
import com.example.realestate.database.table.UserAccountTable;


public interface ContentDescriptor {
	static final String AUTHORITY 			= "com.ngocketit.realestatebroker.provider.contentprovider";
	static final String CONTENT_TYPE 		= "vnd.android.cursor.dir/vnd.com.ngocketit.realestatebroker";
	static final String CONTENT_ITEM_TYPE	= "vnd.android.cursor.item/vnd.com.ngocketit.realestatebroker";
	
	static final String BASE_CONTENT_URI_PATH = "content://" + AUTHORITY + "/";

	static final int BASE_ITEMS_QUERY_TYPE_INDEX 	= 100;
	static final int BASE_ITEM_ID_QUERY_TYPE_INDEX 	= 200;
	static final int BASE_EXTENDED_QUERY_TYPE_INDEX	= 300;

	public interface ContentPath {
		static final String CUSTOMER 			= CustomerTable.TABLE_NAME;
		static final String USER_ACCOUNT 		= UserAccountTable.TABLE_NAME;
		static final String PROPERTY 			= PropertyTable.TABLE_NAME;
		static final String VOCABULARY	 		= TaxonomyTable.TABLE_NAME;
        static final String APPOINTMENT	 		= AppointmentTable.TABLE_NAME;
        static final String LEAD	 		    = LeadTable.TABLE_NAME;
        static final String ITEM_ATTRIBUTE	    = ItemAttributeTable.TABLE_NAME;
        static final String ITEM_TAXONOMY       = ItemVocabularyTable.TABLE_NAME;
        static final String ITEM_ATTACHMENT     = ItemAttachmentTable.TABLE_NAME;

		public static interface ExtendedPath {
			static final String CUSTOMER_GROUP_BY_LOCALITY = "customer_group_by_locality";
			static final String PROPERTY_GROUP_BY_CATEGORY = "property_group_by_category";
            static final String PROPERTY_GROUP_BY_LOCALITY = "property_group_by_locality";
            static final String PROPERTY_LATEST            = "property_latest";
			static final String VOCABULARY_GROUP_BY_PARENT = "vocabulary_group_by_parent";
            static final String APPOINTMENT_GROUP_BY_WEEK  = "appointment_group_by_week";
            static final String APPOINTMENT_MOST_DUE       = "appointment_most_due";
		}
	}
	
	public interface ContentUri {
		static final Uri CUSTOMER 			= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.CUSTOMER);
		static final Uri USER_ACCOUNT 		= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.USER_ACCOUNT);
		static final Uri PROPERTY 			= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.PROPERTY);
		static final Uri VOCABULARY 		= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.VOCABULARY);
        static final Uri APPOINTMENT 		= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.APPOINTMENT);
        static final Uri ITEM_ATTRIBUTE     = Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ITEM_ATTRIBUTE);
        static final Uri ITEM_TAXONOMY      = Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ITEM_TAXONOMY);
        static final Uri ITEM_ATTACHMENT    = Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ITEM_ATTACHMENT);

		public interface ExtendedUri {
			static final Uri CUSTOMER_GROUP_BY_LOCALITY = Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ExtendedPath.CUSTOMER_GROUP_BY_LOCALITY);
			static final Uri PROPERTY_GROUP_BY_CATEGORY	= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ExtendedPath.PROPERTY_GROUP_BY_CATEGORY);
            static final Uri PROPERTY_GROUP_BY_LOCALITY	= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ExtendedPath.PROPERTY_GROUP_BY_LOCALITY);
            static final Uri PROPERTY_LATEST	        = Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ExtendedPath.PROPERTY_LATEST);
			static final Uri VOCABULARY_GROUP_BY_PARENT	= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ExtendedPath.VOCABULARY_GROUP_BY_PARENT);
            static final Uri APPOINTMENT_GROUP_BY_WEEK	= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ExtendedPath.APPOINTMENT_GROUP_BY_WEEK);
            static final Uri APPOINTMENT_MOST_DUE   	= Uri.parse(BASE_CONTENT_URI_PATH + ContentPath.ExtendedPath.APPOINTMENT_MOST_DUE);
		}
	}
	
	public interface QueryType {
		static final int CUSTOMER			= BASE_ITEMS_QUERY_TYPE_INDEX + 1;
		static final int USER_ACCOUNT		= BASE_ITEMS_QUERY_TYPE_INDEX + 2;
		static final int VOCABULARY			= BASE_ITEMS_QUERY_TYPE_INDEX + 3;
		static final int PROPERTY			= BASE_ITEMS_QUERY_TYPE_INDEX + 4;
        static final int APPOINTMENT		= BASE_ITEMS_QUERY_TYPE_INDEX + 5;
        static final int LEAD		        = BASE_ITEMS_QUERY_TYPE_INDEX + 6;
        static final int ITEM_ATTRIBUTE		= BASE_ITEMS_QUERY_TYPE_INDEX + 7;
        static final int ITEM_TAXONOMY		= BASE_ITEMS_QUERY_TYPE_INDEX + 8;
        static final int ITEM_ATTACHMENT    = BASE_ITEMS_QUERY_TYPE_INDEX + 9;

		static final int CUSTOMER_ID			= BASE_ITEM_ID_QUERY_TYPE_INDEX + 20;
		static final int USER_ACCOUNT_ID		= BASE_ITEM_ID_QUERY_TYPE_INDEX + 21;
		static final int VOCABULARY_ID			= BASE_ITEM_ID_QUERY_TYPE_INDEX + 22;
		static final int PROPERTY_ID			= BASE_ITEM_ID_QUERY_TYPE_INDEX + 23;
        static final int APPOINTMENT_ID			= BASE_ITEM_ID_QUERY_TYPE_INDEX + 24;
        static final int LEAD_ID			    = BASE_ITEM_ID_QUERY_TYPE_INDEX + 25;

		public interface ExtendedQueryType {
			static final int CUSTOMER_GROUP_BY_LOCALITY = BASE_EXTENDED_QUERY_TYPE_INDEX + 1;
			static final int PROPERTY_GROUP_BY_CATEGORY = BASE_EXTENDED_QUERY_TYPE_INDEX + 2;
			static final int VOCABULARY_GROUP_BY_PARENT = BASE_EXTENDED_QUERY_TYPE_INDEX + 3;
            static final int APPOINTMENT_GROUP_BY_WEEK  = BASE_EXTENDED_QUERY_TYPE_INDEX + 4;
            static final int APPOINTMENT_MOST_DUE       = BASE_EXTENDED_QUERY_TYPE_INDEX + 5;
            static final int PROPERTY_GROUP_BY_LOCALITY = BASE_EXTENDED_QUERY_TYPE_INDEX + 6;
            static final int PROPERTY_LATEST            = BASE_EXTENDED_QUERY_TYPE_INDEX + 7;
		}
	}
}
