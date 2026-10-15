CREATE TABLE anuncio (
    id         BIGSERIAL     PRIMARY KEY,
    usuario_id BIGINT        NOT NULL REFERENCES usuario (id),
    titulo     VARCHAR(200)  NOT NULL,
    artista    VARCHAR(200)  NOT NULL,
    formato    VARCHAR(10)   NOT NULL CHECK (formato IN ('CD', 'DVD')),
    estado     VARCHAR(20)   NOT NULL CHECK (estado IN ('NOVO', 'SEMINOVO', 'USADO', 'DESGASTADO')),
    preco      NUMERIC(10,2) NOT NULL CHECK (preco >= 0),
    status     VARCHAR(15)   NOT NULL DEFAULT 'DISPONIVEL' CHECK (status IN ('DISPONIVEL', 'NEGOCIADO')),
    criado_em  TIMESTAMP     NOT NULL DEFAULT now()
);
CREATE INDEX idx_anuncio_usuario ON anuncio (usuario_id);
CREATE INDEX idx_anuncio_formato_status ON anuncio (formato, status);
