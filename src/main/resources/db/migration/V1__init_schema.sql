create table admin_user (
    id bigserial primary key,
    username varchar(100) not null unique,
    password_hash varchar(255) not null,
    created_at timestamptz not null default current_timestamp,
    updated_at timestamptz not null default current_timestamp
);

create table project (
    id bigserial primary key,
    title varchar(160) not null,
    slug varchar(180) not null unique,
    category varchar(120) not null,
    description varchar(4000) not null,
    tools_used varchar(500),
    created_for varchar(255),
    project_date date,
    featured boolean not null default false,
    published boolean not null default false,
    sort_order integer not null default 0,
    created_at timestamptz not null default current_timestamp,
    updated_at timestamptz not null default current_timestamp,
    constraint chk_project_sort_order_non_negative check (sort_order >= 0)
);

create table project_image (
    id bigserial primary key,
    project_id bigint not null references project(id) on delete cascade,
    file_name varchar(255) not null,
    original_file_name varchar(255) not null,
    file_path varchar(500) not null,
    alt_text varchar(255),
    display_order integer not null default 0,
    is_cover boolean not null default false,
    created_at timestamptz not null default current_timestamp,
    updated_at timestamptz not null default current_timestamp,
    constraint chk_project_image_display_order_non_negative check (display_order >= 0)
);

create index idx_project_published_featured_sort on project (published, featured desc, sort_order asc, project_date desc);
create index idx_project_category on project (category);
create index idx_project_image_project_display on project_image (project_id, display_order, id);
create unique index ux_project_image_cover_per_project on project_image(project_id) where is_cover = true;
