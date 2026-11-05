-- Tabel untuk Fitur 1: Manajemen Tugas Kuliah
create table public.tasks (
    id uuid default gen_random_uuid() primary key,
    user_id uuid references auth.users(id) on delete cascade not null,
    judul text not null,
    mata_kuliah text not null,
    deskripsi text not null default '',
    deadline date not null,
    prioritas text not null default 'Medium'
        check (prioritas in ('High', 'Medium', 'Low')),
    status text not null default 'To Do'
        check (status in ('To Do', 'In Progress', 'Completed')),
    created_at timestamptz default now() not null
);

alter table public.tasks enable row level security;

create policy "Pengguna dapat membaca tugas sendiri"
on public.tasks for select
using ((select auth.uid()) = user_id);

create policy "Pengguna dapat menambah tugas sendiri"
on public.tasks for insert
with check ((select auth.uid()) = user_id);

create policy "Pengguna dapat mengubah tugas sendiri"
on public.tasks for update
using ((select auth.uid()) = user_id)
with check ((select auth.uid()) = user_id);

create policy "Pengguna dapat menghapus tugas sendiri"
on public.tasks for delete
using ((select auth.uid()) = user_id);
