package com.example.realestate.database.query;


import com.example.realestate.database.TableColumn;
import com.example.realestate.database.table.ItemVocabularyTable;
import com.example.realestate.database.table.TaxonomyTable;

public interface RawQuery {
    static final String CUSTOMER_DETAIL = "SELECT C.*, V.title AS locality_name " +
            "FROM customers AS C " +
            "LEFT JOIN vocabularies V ON C.locality_id = V._id " +
            "WHERE C._id = ?";

    static final String TAXONOMY_CHILD_COUNT = "SELECT COUNT(*) AS _count FROM vocabularies WHERE parent_id = ?";

    static final SelectQuery TAXONOMY_GROUP_BY_PARENT = new SelectQuery(
            "SELECT C._id, C.title, " +
            "   (CASE WHEN C.parent_id > 0 THEN P.title ELSE C.title END) AS parent_name, " +
            "   P._id AS parent_id " +
            "FROM vocabularies AS C " +
            "LEFT JOIN vocabularies AS P ON C.parent_id = P._id",
            null,
            "GROUP BY parent_name, C._id ORDER BY parent_name ASC, parent_id ASC, C.title ASC"
    );

    static final SelectQuery PROPERTY_GROUP_BY_LOCALITY = new SelectQuery(
            "SELECT P._id, P.locality_id, P.price, " +
            "   C.description AS currency_symbol, " +
            "   C.title AS currency_name," +
            "   P.name, P.cover_pic_path, " +
            "   L.title AS locality_name " +
            "FROM properties AS P " +
            "LEFT JOIN vocabularies AS L ON P.locality_id = L._id " +
            "LEFT JOIN vocabularies AS C ON P.price_unit_id = C._id ",
            null,
            "GROUP BY P._id, P.locality_id ORDER BY L.title ASC"
    );

    static final String PROPERTY_LATEST = "SELECT P._id, P.locality_id, P.price, " +
            "   C.description AS currency_symbol, " +
            "   C.title AS currency_name," +
            "   P.name, P.cover_pic_path, " +
            "   L.title AS locality_name " +
            "FROM properties AS P " +
            "LEFT JOIN vocabularies AS L ON P.locality_id = L._id " +
            "LEFT JOIN vocabularies AS C ON P.price_unit_id = C._id " +
            "ORDER BY P.created_at DESC " +
            "LIMIT ?";

    static final String PROPERTY_DETAIL = "SELECT P.*, C.name AS customer_name, " +
            "   C.thumbnail_path " +
            "FROM properties P " +
            "LEFT JOIN customers C ON P.customer_id = C._id " +
            "WHERE P._id=?";

    static final SelectQuery CUSTOMER_GROUP_BY_LOCALITY = new SelectQuery(
            "SELECT C._id, C.locality_id, C.type, " +
            "   C.name, C.thumbnail_path, " +
            "   L.title AS locality_name, " +
            "   COUNT(P._id) AS property_count " +
            "FROM customers AS C " +
            "LEFT JOIN vocabularies AS L ON C.locality_id = L._id " +
            "LEFT JOIN properties AS P ON P.customer_id = C._id ",
            null,
            "GROUP BY C._id, C.locality_id ORDER BY L.title ASC"
    );

    static final SelectQuery APPOINTMENT_GROUP_BY_WEEK = new SelectQuery(
            "SELECT A.*, C.name AS customer_name, C.thumbnail_path, " +
            "   strftime('%W', A.from_time) AS week_number, " +
            "   COUNT(R.appointment_id) AS reminder_count " +
            "FROM appointments AS A " +
            "LEFT JOIN customers AS C ON A.customer_id = C._id " +
            "LEFT JOIN appointment_reminders AS R ON R.appointment_id = A._id ",
            null,
            "GROUP BY A._id, week_number " +
            "ORDER BY A.from_time ASC"
    );

    static final String APPOINTMENTS_MOST_DUE = "SELECT A.*, C.name AS customer_name, C.thumbnail_path, " +
            "COUNT(R.appointment_id) AS reminder_count " +
            "FROM appointments AS A " +
            "LEFT JOIN customers AS C ON A.customer_id = C._id " +
            "LEFT JOIN appointment_reminders AS R ON R.appointment_id = A._id " +
            "WHERE A.from_time >= ? " +
            "GROUP BY A._id " +
            "ORDER BY A.from_time ASC " +
            "LIMIT ?";

    static final String APPOINTMENT_DETAIL = "SElECT A.*, GROUP_CONCAT(R.reminder_time, ';') AS reminders, " +
            "C.name AS customer_name, C.thumbnail_path, C._id AS customer_id " +
            "FROM appointments A " +
            "LEFT JOIN appointment_reminders R ON A._id = R.appointment_id " +
            "LEFT JOIN customers C ON A.customer_id = C._id " +
            "WHERE A._id = ? ";

    static final String ITEM_TAXONOMY = "SELECT T.* FROM " + ItemVocabularyTable.TABLE_NAME + " AS I " +
            "INNER JOIN " + TaxonomyTable.TABLE_NAME + " AS T ON T._id=I." + TableColumn.TAXONOMY_ID + " " +
            "WHERE I.item_id=?";


	public class SelectQuery {
		public String selectClause;
		public String whereClause;
		public String suffixClause;

		public SelectQuery(String select, String where, String suffix) {
			selectClause = select;
			whereClause = where;
			suffixClause = suffix;
		}

		public String toString() {
			String query = selectClause;

			if (whereClause != null) {
				query += " WHERE " + whereClause;
			}

			if (suffixClause != null) {
				query += " " + suffixClause;
			}

			return query;
		}

        /**
         * This contains changes to whereClause so we need to make sure that
         * different threads don't at the same time overwrite that value.
         *
         * @param extraWhereClause
         * @return
         */
		public synchronized String toString(String extraWhereClause) {
			if (extraWhereClause == null) {
				return toString();
			}

			String oldWhere = whereClause;

			if (whereClause == null) {
				whereClause = extraWhereClause;
			} else {
				whereClause += " " + extraWhereClause;
			}

			String query = toString();
			whereClause = oldWhere;

			return query;
		}
	}
}
