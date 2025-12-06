# High level system architecture
```mermaid
flowchart LR

subgraph ClientSide
    A[Vue + TipTap Editor] -->|WS / HTTP| B[API Gateway / BFF]
    C[Browser Storage &lpar;offline&rpar;]
end

B --> D[Auth Service &lpar;JWT&rpar;]
B --> E[Document Service &lpar;Spring Boot&rpar;]
B --> F[Collaboration Service &lpar;Yjs WebSocket&rpar;]
B --> G[Comment Service]
B --> H[Search Service &lpar;Elastic / Vector DB&rpar;]
B --> I[RAG Service in Python]
B --> J[Notification Service]
B --> K[Audit Service]
B --> L[File/Media Service &lpar;S3&rpar;]

subgraph Infra
    M[(Postgres)]
    N[(Redis)]
    O((Kafka))
    P[(Elasticsearch / Pinecone)]
    Q[(Object Storage S3)]
end

E --> M
G --> M
K --> M

F --> N
F --> O

E --> O
G --> O
J --> O
I --> O

I --> P
L --> Q

style ClientSide fill:#f9f,stroke:#333,stroke-width:1px
style Infra fill:#efe,stroke:#333,stroke-width:1px
```
---
# Deployment/ Runtime view
```mermaid
flowchart LR

subgraph Kubernetes Cluster
    sub1[API Gateway / BFF Replicas]
    sub2[Auth Service Replicas]
    sub3[Document Service Replicas]
    sub4[Collab Service as Stateful]
    sub5[Comment Service Replicas]
    sub6[Search Service Replicas]
    sub7[RAG Service Python Replicas]
    sub8[Notification Worker Replicas]
    sub9[Audit Worker]
end

sub1 -->|HTTP/HTTPS| sub3
sub1 --> sub4
sub1 --> sub5
sub1 --> sub7

sub3 --> Postgres[(Postgres Cluster)]
sub4 --> Redis[(Redis Cluster)]
sub4 --> Kafka[(Kafka Cluster)]
sub3 --> Kafka
sub5 --> Kafka
sub7 --> VectorDB[(Pinecone / Milvus / Weaviate)]
sub6 --> Elastic[(Elasticsearch Cluster)]
sub8 --> Email[(Email Provider / SES)]
sub3 --> S3[(Object Storage / S3)]
```

# Sequence Diagram — Create Document & Notify
```mermaid
sequenceDiagram
participant Client
participant BFF as API Gateway/BFF
participant Auth
participant Doc as Document Service
participant Kafka
participant Notif as Notification Service
participant Email


Client->>BFF: POST /documents {payload}
BFF->>Auth: Validate JWT
Auth-->>BFF: user validated
BFF->>Doc: Create document (store metadata, initial version)
Doc-->>Kafka: emit document.created {docId, ownerId}
Kafka-->>Notif: (consumed)
Notif->>Email: send in-app + email notifications
Email-->>Notif: delivery status
Notif-->>Kafka: notification.sent
Doc-->>BFF: 201 Created {docId}
BFF-->>Client: 201 Created {docId}
```

---

# Sequence Diagram — Real-time Collaboration (Edit flow using CRDT/Yjs)
```mermaid
sequenceDiagram
participant UserA as Editor A (Browser)
participant WS as Collab WS (Yjs)
participant Collab as Collaboration Service
participant OpsStore as Document Service (Ops Log)
participant Kafka


UserA->>WS: Join room (docId) / Awareness
WS->>Collab: connect userA
UserA->>WS: Apply local change (Yjs update)
WS->>Collab: Yjs delta (op)
Collab->>WS: Broadcast delta to connected clients
Collab->>OpsStore: Persist op to ops-log (async)
OpsStore-->>Kafka: emit document.segment.updated {opMetadata}
Kafka->>OtherServices: index/update/notify
```

# Sequence Diagram — Commenting → Notification → In-app & Email
```mermaid
sequenceDiagram
participant Client
participant BFF
participant Auth
participant Comment
participant Kafka
participant Notif
participant Email
participant InApp as ClientPush


Client->>BFF: POST /documents/{id}/comments
BFF->>Auth: validate
BFF->>Comment: create comment
Comment-->>Kafka: comment.created {commentId, docId, parentId}
Kafka->>Notif: (consumed)
Notif->>Email: send email
Notif->>InApp: push websocket notification
InApp-->>Client: show notification
Comment-->>BFF: 201 Created {commentId}
BFF-->>Client: 201 Created
```

# Sequence Diagram — RAG Query (Ask about Document)
```mermaid
sequenceDiagram
participant Client
participant BFF
participant Auth
participant Rag as RAG Service
participant Vector as Vector DB
participant Cache as Redis
participant Doc as Document Service


Client->>BFF: POST /documents/{id}/rag-query {question}
BFF->>Auth: validate
BFF->>Doc: optional: fetch metadata/permissions
BFF->>Cache: check cached answer
alt cache hit
Cache-->>BFF: cached answer
BFF-->>Client: cached answer
else cache miss
BFF->>Rag: request (docId, question)
Rag->>Vector: embed(question) + search(doc_vectors)
Vector-->>Rag: top_k references
Rag->>Doc: fetch referenced segments
Rag->>LLM: call model with context
LLM-->>Rag: answer
Rag->>Cache: store answer
Rag-->>BFF: answer
BFF-->>Client: answer
end
```
