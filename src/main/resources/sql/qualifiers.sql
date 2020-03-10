insert
  into time_series_qualifiers(json_data_id,
                              start_time,
                              end_time,
                              qualifier_user,
                              identifier,
                              date_applied_utc
                             )
select json_data_id,
       adjust_timestamp(jsonb_extract_path_text(qualifiers, 'StartTime')) start_time,
       adjust_timestamp(jsonb_extract_path_text(qualifiers, 'EndTime')) end_time,
       jsonb_extract_path_text(qualifiers, 'User') qualifier_user,
       jsonb_extract_path_text(qualifiers, 'Identifier') identifier,
       adjust_timestamp(jsonb_extract_path_text(qualifiers, 'DateApplied')) date_applied_utc
  from (select json_data_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'Qualifiers')) qualifiers
          from json_data
         where json_data_id = ?) a
