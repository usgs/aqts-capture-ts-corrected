insert
  into time_series_approvals (time_series_unique_id,
                              start_time,
                              end_time,
                              approval_user,
                              comment,
                              approval_level,
                              level_description,
                              date_applied_utc,
                              response_time,
                              response_version
                             )
select time_series_unique_id,
       jsonb_extract_path_text(approvals, 'StartTime')::timestamp start_time,
       jsonb_extract_path_text(approvals, 'EndTime')::timestamp end_time,
       jsonb_extract_path_text(approvals, 'User') approval_user,
       jsonb_extract_path_text(approvals, 'Comment') "comment",
       jsonb_extract_path_text(approvals, 'ApprovalLevel')::numeric approval_level,
       jsonb_extract_path_text(approvals, 'LevelDescription') level_description,
       jsonb_extract_path_text(approvals, 'DateAppliedUtc')::timestamp date_applied_utc,
       response_time,
       response_version
  from (select jsonb_extract_path_text(json_content, 'UniqueId') time_series_unique_id,
               jsonb_array_elements(jsonb_extract_path(json_content, 'Approvals')) approvals,
               jsonb_extract_path_text(json_content, 'ResponseTime')::timestamp response_time,
               jsonb_extract_path_text(json_content, 'ResponseVersion')::numeric response_version
          from json_data
         where json_data_id = ?) a
