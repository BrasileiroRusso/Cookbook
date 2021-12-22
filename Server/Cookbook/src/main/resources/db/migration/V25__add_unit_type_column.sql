ALTER TABLE unit ADD COLUMN unit_type_id bigint REFERENCES unit_type (id),
                 ADD COLUMN measure numeric(28, 10),
                 ADD COLUMN is_main boolean;