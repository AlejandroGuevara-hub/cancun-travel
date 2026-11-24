-- Actualiza presupuestos de la versi?n anterior antes de que Hibernate lea el enum.
DO $$
DECLARE constraint_name text;
BEGIN
    IF to_regclass('public.trip') IS NOT NULL THEN
        ALTER TABLE public.trip ADD COLUMN IF NOT EXISTS version bigint NOT NULL DEFAULT 0;
    END IF;
    IF to_regclass('public.budget') IS NOT NULL THEN
        FOR constraint_name IN
            SELECT conname FROM pg_constraint
            WHERE conrelid = 'public.budget'::regclass AND contype = 'c'
              AND pg_get_constraintdef(oid) ~* '\mstatus\M'
        LOOP
            EXECUTE format('ALTER TABLE public.budget DROP CONSTRAINT %I', constraint_name);
        END LOOP;
        UPDATE public.budget SET status = 'PENDIENTE_ADMIN' WHERE status = 'PENDIENTE';
        ALTER TABLE public.budget ADD CONSTRAINT budget_status_sprint2_check
            CHECK (status IN ('PENDIENTE_ADMIN', 'PENDIENTE_DUENO', 'APROBADO', 'RECHAZADO'));
    END IF;
END $$;@@
