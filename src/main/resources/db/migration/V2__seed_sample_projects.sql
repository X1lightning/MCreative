insert into project (
    id, title, slug, category, description, tools_used, created_for, project_date,
    featured, published, sort_order
) values
    (
        1,
        'Northline Coffee Identity',
        'northline-coffee-identity',
        'Brand Identity',
        'A warm, editorial brand system for a student-run coffee concept, spanning packaging, menu layouts, and social launch graphics.',
        'Adobe Illustrator, Adobe InDesign, Figma',
        'Concept Studio Brief',
        date '2025-10-14',
        true,
        true,
        1
    ),
    (
        2,
        'Common Thread Editorial Spread',
        'common-thread-editorial-spread',
        'Editorial',
        'A magazine feature exploring local makers with restrained typography, modular image placement, and a tactile print-first pacing.',
        'Adobe InDesign, Photoshop',
        'Typography Course',
        date '2025-09-02',
        true,
        true,
        2
    ),
    (
        3,
        'Field Notes Poster Series',
        'field-notes-poster-series',
        'Poster Design',
        'A small poster system balancing strong grid structure with expressive color blocking and succinct event messaging.',
        'Illustrator, Risograph Prep',
        'Campus Exhibition',
        date '2025-08-11',
        false,
        true,
        3
    )
on conflict (id) do nothing;

select setval('project_id_seq', greatest((select coalesce(max(id), 1) from project), 1), true);

insert into project_image (
    id, project_id, file_name, original_file_name, file_path, alt_text, display_order, is_cover
) values
    (1, 1, 'cover-demo-1.jpg', 'northline-cover.jpg', 'projects/northline-coffee-identity/cover-demo-1.jpg', 'Coffee bag mockup with wordmark', 0, true),
    (2, 1, 'detail-demo-1.jpg', 'northline-menu.jpg', 'projects/northline-coffee-identity/detail-demo-1.jpg', 'Folded menu layout', 1, false),
    (3, 2, 'cover-demo-2.jpg', 'common-thread-cover.jpg', 'projects/common-thread-editorial-spread/cover-demo-2.jpg', 'Editorial spread with portrait and serif headline', 0, true),
    (4, 3, 'cover-demo-3.jpg', 'field-notes-cover.jpg', 'projects/field-notes-poster-series/cover-demo-3.jpg', 'Poster wall with bold typography', 0, true)
on conflict (id) do nothing;

select setval('project_image_id_seq', greatest((select coalesce(max(id), 1) from project_image), 1), true);
