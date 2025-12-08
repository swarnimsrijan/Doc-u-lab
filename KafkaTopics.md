# Kafka Topics (Event-Driven Architecture)
## User Service
- user.registered
- user.blocked
- user.unblocked
- user.role.updated

## Document Service
- document.created
- document.updated
- document.deleted
- document.version.created
- document.segment.updated
- document.shared
- document.access.requested
- document.access.granted

## Comment Service
- comment.created
- comment.replied
- comment.deleted

## Notification Service
- notification.send
- notification.retry
- notification.failed

## Audit Trail
- audit.log

## Search Indexing
- search.index.update
- search.index.delete

## RAG Service
- rag.query.requested
- rag.query.processed
- rag.cache.updated

## Optional Additional Events
### Conflict Resolution
- document.segment.conflict.detected
- document.segment.conflict.resolved

### Real-time collaboration
- collab.cursor.position
- collab.presence.update
- collab.typing.event