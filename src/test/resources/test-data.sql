insert into member(create_date, hits, email, user_id, password, role, social_type) values ('2023-04-30 12:59:00', 0, 'user@kakao.com', 'user', '$2a$10$lok4/duGF6yAJjAF4oSyhOZG.2uWr0CCorCZilInWKUuS/FCXMvZe', 'USER', 'NONE');
insert into member(create_date, hits, email, user_id, password, role, social_type) values ('2023-04-30 12:59:00', 0, 'admin@kakao.com', 'admin', '$2a$10$lok4/duGF6yAJjAF4oSyhOZG.2uWr0CCorCZilInWKUuS/FCXMvZe', 'ADMIN', 'NONE');
insert into member(create_date, hits, email, user_id, password, role, social_type) values ('2023-04-30 12:59:00', 0, 'admin@kakao.com', 'admin', '$2a$10$lok4/duGF6yAJjAF4oSyhOZG.2uWr0CCorCZilInWKUuS/FCXMvZe', 'USER', 'KAKAO');

insert into flower(create_date, `name`, flower_lang, description) values ('2023-04-30 12:59:00', '라일락', '젊은 날의 추억', '라일락(Lilac)은 꿀풀목 물푸레나무과의 낙엽 활엽 소교목이다.');
insert into flower(create_date, `name`, flower_lang, description) values ('2023-04-30 12:59:00', '철쭉', '정열, 명예', '철쭉은 종류도 많고 이름도 가지가지다.');

insert into flower_like(create_date, flower_id, member_id) values ('2023-04-30 12:59:00', 1, 1);
insert into flower_like(create_date, flower_id, member_id) values ('2023-04-30 12:59:00', 1, 2);
insert into flower_like(create_date, flower_id, member_id) values ('2023-04-30 12:59:00', 2, 3);


