-- USER and Auth tables
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- USERS
CREATE TABLE users (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email           VARCHAR(255) UNIQUE NOT NULL,
    full_name       VARCHAR(255) NOT NULL,
    hashed_password TEXT NOT NULL,
    is_blocked      BOOLEAN DEFAULT FALSE,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);

-- ROLES (Admin, Owner, Editor, Reader)
CREATE TABLE roles (
    id          SMALLINT PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO roles(id, name) VALUES
(1, 'ADMIN'), (2, 'OWNER'), (3, 'EDITOR'), (4, 'READER');

-- USER ROLE ASSIGNMENTS PER DOCUMENT
CREATE TABLE user_document_roles (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL REFERENCES users(id),
    document_id     UUID NOT NULL,
    role_id         SMALLINT NOT NULL REFERENCES roles(id),
    granted_by      UUID REFERENCES users(id),
    created_at      TIMESTAMP DEFAULT NOW(),
    UNIQUE (user_id, document_id)
);
-- Document Tables
CREATE TABLE documents (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                VARCHAR(255) NOT NULL,
    meme_name           VARCHAR(255),
    type                VARCHAR(50) NOT NULL, -- pdf, docx, markdown, yaml, xml, mermaid
    owner_id            UUID NOT NULL REFERENCES users(id),
    current_version_id  UUID,
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_documents_type ON documents(type);

-- DOCUMENT VERSIONS
CREATE TABLE document_versions (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id     UUID NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    version_number  INT NOT NULL,
    change_summary  TEXT,
    created_by      UUID NOT NULL REFERENCES users(id),
    created_at      TIMESTAMP DEFAULT NOW(),
    UNIQUE (document_id, version_number)
);

-- Document Segments (for collaboration & partial edits)
CREATE TABLE document_segments (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id     UUID NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    version_id      UUID NOT NULL REFERENCES document_versions(id) ON DELETE CASCADE,
    segment_index   INT NOT NULL,
    content         JSONB NOT NULL, -- text, image metadata, code, etc.
    media_type      VARCHAR(50) NOT NULL, -- text/image/code
    updated_by      UUID REFERENCES users(id),
    updated_at      TIMESTAMP DEFAULT NOW(),

    UNIQUE (document_id, version_id, segment_index)
);

-- Collaboration Contributions
CREATE TABLE segment_contributors (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    segment_id      UUID NOT NULL REFERENCES document_segments(id) ON DELETE CASCADE,
    user_id         UUID NOT NULL REFERENCES users(id),
    contributed_at  TIMESTAMP DEFAULT NOW()
);

-- Comments (Threaded like Reddit)
CREATE TABLE comments (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id         UUID NOT NULL REFERENCES documents(id),
    segment_id          UUID REFERENCES document_segments(id),
    parent_id           UUID REFERENCES comments(id) ON DELETE CASCADE,
    commentor_id        UUID NOT NULL REFERENCES users(id),
    comment_type        VARCHAR(50) NOT NULL, -- Bug, Conflict, Doubt, Suggestion
    target_user_id      UUID REFERENCES users(id),
    text                TEXT NOT NULL,
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP,
    deleted_at          TIMESTAMP
);

CREATE INDEX idx_comments_document ON comments(document_id);

-- Notification Tracking
CREATE TABLE notification_events (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_type      VARCHAR(100) NOT NULL,
    document_id     UUID,
    user_id         UUID,
    payload         JSONB,
    created_at      TIMESTAMP DEFAULT NOW(),
    processed       BOOLEAN DEFAULT FALSE
);

-- Audit Log (append-only)
CREATE TABLE audit_logs (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    actor_user_id   UUID REFERENCES users(id),
    document_id     UUID,
    action          VARCHAR(255) NOT NULL,
    metadata        JSONB,
    created_at      TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_audit_document ON audit_logs(document_id);

-- Search Index Table (optional local EP cache)
CREATE TABLE search_index (
    document_id     UUID PRIMARY KEY REFERENCES documents(id) ON DELETE CASCADE,
    content_vector  TSVECTOR,
    updated_at      TIMESTAMP DEFAULT NOW()
);

-- RAG Metadata Cache
CREATE TABLE rag_cache (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id     UUID NOT NULL REFERENCES documents(id),
    query_text      TEXT NOT NULL,
    answer_text     TEXT NOT NULL,
    created_at      TIMESTAMP DEFAULT NOW(),
    UNIQUE (document_id, query_text)
);
