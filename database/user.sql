-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER bucket_list_owner
WITH PASSWORD 'finalcapstone';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO bucket_list_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO bucket_list_owner;

CREATE USER bucket_list_appuser
WITH PASSWORD 'finalcapstone';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO bucket_list_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO bucket_list_appuser;
