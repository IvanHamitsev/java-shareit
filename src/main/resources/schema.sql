CREATE TABLE IF NOT EXISTS USERS (
	USER_ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
	NAME VARCHAR(255) NOT NULL,
	EMAIL VARCHAR(255) UNIQUE NOT NULL,
	LOGIN VARCHAR(255) UNIQUE NOT NULL,
	BIRTHDAY TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS ITEM_REQUESTS (
	ITEM_REQ_ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
	USER_ID BIGINT REFERENCES USERS(USER_ID) ON DELETE CASCADE NOT NULL,
	NAME VARCHAR(255) NOT NULL,
	DESCRIPTION VARCHAR(512) NOT NULL,
	REQUEST_DATE TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS ITEMS (
	ITEM_ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
	NAME VARCHAR(255) NOT NULL,
	DESCRIPTION VARCHAR(512) NOT NULL,
	OWNER_ID BIGINT REFERENCES USERS(USER_ID) ON DELETE CASCADE NOT NULL,
	REQUEST_ID BIGINT REFERENCES ITEM_REQUESTS(ITEM_REQ_ID) ON DELETE CASCADE,
	IS_AVAILABLE_FOR_RENT BOOL NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS ITEM_RESPONSES (
	ITEM_RESP_ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
	USER_ID BIGINT REFERENCES USERS(USER_ID) ON DELETE CASCADE NOT NULL,
	ITEM_ID BIGINT REFERENCES ITEMS(ITEM_ID) ON DELETE CASCADE NOT NULL,
	NAME VARCHAR(255),
	DESCRIPTION VARCHAR(512) NOT NULL,
	RESPONSE_DATE TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS BOOKINGS (
	BOOKING_ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
	USER_ID BIGINT REFERENCES USERS(USER_ID) ON DELETE CASCADE NOT NULL,
	ITEM_ID BIGINT REFERENCES ITEMS(ITEM_ID) ON DELETE CASCADE NOT NULL,
	STATUS VARCHAR(128) NOT NULL,
	BOOKING_START TIMESTAMP WITH TIME ZONE NOT NULL,
	BOOKING_END TIMESTAMP WITH TIME ZONE
);
