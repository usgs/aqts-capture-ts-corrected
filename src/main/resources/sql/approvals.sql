insert
  into time_series_approvals (json_data_id,
                              start_time,
                              end_time,
                              approval_user,
                              approval_comment,
                              approval_level,
                              level_description,
                              date_applied_utc
                             )
select json_data_id,
       adjust_timestamp(jsonb_extract_path_text(approvals, 'StartTime')) start_time,
       adjust_timestamp(jsonb_extract_path_text(approvals, 'EndTime')) end_time,
       jsonb_extract_path_text(approvals, 'User') approval_user,
       jsonb_extract_path_text(approvals, 'Comment') approval_comment,
       jsonb_extract_path_text(approvals, 'ApprovalLevel')::numeric approval_level,
       jsonb_extract_path_text(approvals, 'LevelDescription') level_description,
       adjust_timestamp(jsonb_extract_path_text(approvals, 'DateAppliedUtc')) date_applied_utc
  from (select json_data_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'Approvals')) approvals
          from json_data
         where json_data_id = ?) a
