-- Jalankan skrip ini sekali di Supabase Dashboard > SQL Editor.
-- Tabel ini harus berada pada schema public karena aplikasi memanggilnya
-- dengan nama: catatan_mata_kuliah, catatan_pertemuan, dan catatan_isi.

create table if not exists public.catatan_mata_kuliah (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references auth.users(id) on delete cascade,
    semester integer not null check (semester between 1 and 8),
    nama text not null check (length(trim(nama)) > 0),
    created_at timestamptz not null default timezone('utc', now())
);

create table if not exists public.catatan_pertemuan (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references auth.users(id) on delete cascade,
    mata_kuliah_id uuid not null
        references public.catatan_mata_kuliah(id) on delete cascade,
    nomor integer not null check (nomor > 0),
    created_at timestamptz not null default timezone('utc', now()),
    unique (mata_kuliah_id, nomor)
);

create table if not exists public.catatan_isi (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references auth.users(id) on delete cascade,
    pertemuan_id uuid not null
        references public.catatan_pertemuan(id) on delete cascade,
    judul text not null check (length(trim(judul)) > 0),
    isi text not null check (length(trim(isi)) > 0),
    created_at timestamptz not null default timezone('utc', now())
);

create index if not exists catatan_mata_kuliah_user_semester_idx
    on public.catatan_mata_kuliah (user_id, semester);
create index if not exists catatan_pertemuan_user_matkul_idx
    on public.catatan_pertemuan (user_id, mata_kuliah_id);
create index if not exists catatan_isi_user_pertemuan_idx
    on public.catatan_isi (user_id, pertemuan_id);

alter table public.catatan_mata_kuliah enable row level security;
alter table public.catatan_pertemuan enable row level security;
alter table public.catatan_isi enable row level security;

grant select, insert, update, delete on public.catatan_mata_kuliah to authenticated;
grant select, insert, update, delete on public.catatan_pertemuan to authenticated;
grant select, insert, update, delete on public.catatan_isi to authenticated;

drop policy if exists catatan_mata_kuliah_select_own on public.catatan_mata_kuliah;
create policy catatan_mata_kuliah_select_own on public.catatan_mata_kuliah
    for select to authenticated using ((select auth.uid()) = user_id);
drop policy if exists catatan_mata_kuliah_insert_own on public.catatan_mata_kuliah;
create policy catatan_mata_kuliah_insert_own on public.catatan_mata_kuliah
    for insert to authenticated with check ((select auth.uid()) = user_id);
drop policy if exists catatan_mata_kuliah_update_own on public.catatan_mata_kuliah;
create policy catatan_mata_kuliah_update_own on public.catatan_mata_kuliah
    for update to authenticated using ((select auth.uid()) = user_id)
    with check ((select auth.uid()) = user_id);
drop policy if exists catatan_mata_kuliah_delete_own on public.catatan_mata_kuliah;
create policy catatan_mata_kuliah_delete_own on public.catatan_mata_kuliah
    for delete to authenticated using ((select auth.uid()) = user_id);

drop policy if exists catatan_pertemuan_select_own on public.catatan_pertemuan;
create policy catatan_pertemuan_select_own on public.catatan_pertemuan
    for select to authenticated using ((select auth.uid()) = user_id);
drop policy if exists catatan_pertemuan_insert_own on public.catatan_pertemuan;
create policy catatan_pertemuan_insert_own on public.catatan_pertemuan
    for insert to authenticated with check ((select auth.uid()) = user_id);
drop policy if exists catatan_pertemuan_update_own on public.catatan_pertemuan;
create policy catatan_pertemuan_update_own on public.catatan_pertemuan
    for update to authenticated using ((select auth.uid()) = user_id)
    with check ((select auth.uid()) = user_id);
drop policy if exists catatan_pertemuan_delete_own on public.catatan_pertemuan;
create policy catatan_pertemuan_delete_own on public.catatan_pertemuan
    for delete to authenticated using ((select auth.uid()) = user_id);

drop policy if exists catatan_isi_select_own on public.catatan_isi;
create policy catatan_isi_select_own on public.catatan_isi
    for select to authenticated using ((select auth.uid()) = user_id);
drop policy if exists catatan_isi_insert_own on public.catatan_isi;
create policy catatan_isi_insert_own on public.catatan_isi
    for insert to authenticated with check ((select auth.uid()) = user_id);
drop policy if exists catatan_isi_update_own on public.catatan_isi;
create policy catatan_isi_update_own on public.catatan_isi
    for update to authenticated using ((select auth.uid()) = user_id)
    with check ((select auth.uid()) = user_id);
drop policy if exists catatan_isi_delete_own on public.catatan_isi;
create policy catatan_isi_delete_own on public.catatan_isi
    for delete to authenticated using ((select auth.uid()) = user_id);

-- Segarkan cache PostgREST agar tabel baru langsung dikenali API.
notify pgrst, 'reload schema';
