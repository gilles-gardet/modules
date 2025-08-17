DELETE FROM event_publication WHERE serialized_event LIKE '%testuser%';
DELETE FROM event_publication WHERE serialized_event LIKE '%replaytest%';
DELETE FROM event_publication WHERE serialized_event LIKE '%eventtestuser%';

DELETE FROM users WHERE username LIKE '%test%';
DELETE FROM users WHERE email LIKE '%test%';