CREATE TABLE usuario (
    id        BIGSERIAL    PRIMARY KEY,
    nome      VARCHAR(120) NOT NULL,
    email     VARCHAR(200) NOT NULL UNIQUE,
    cidade    VARCHAR(100) NOT NULL,
    criado_em TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_usuario_cidade ON usuario (lower(cidade));
