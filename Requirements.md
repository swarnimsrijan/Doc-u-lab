# DocDost
- A collaborative document editor

## Features to add
- Collaborative Document Editor for multiple editors
- Supports multiple document type
    - MS Word/Google docs
    - PDF
    - Better addd a new type of json doc type
    - YAML
    - Markdown 
    - XML
    - Mermaid
- Supports multiple mediatype
    - Text
    - Image
    - Code
- Role based access(JWT)
    - Admin
    - Owner
    - Editor
    - Reader
- Actions allowed to roles
    - **Reader** 
        - can read any document
    - **Owner**
        - can create any document
        - can read the document which he has created
        - can update or delete the document which he has created
        - can give access to any user for Editor or Reader
    - **Editor**
        - can edit the document
        - can view the document
    - **Admin**
        - all CRUD action related to that document
        - can block any user
        - can restrict any user
        - can delete any owner(once any owner is deleted then the pages and all the user roles associcated with it should be deleted)
- Comments
    - Sync
    - another Microservice
    - Can be added by anyone who has any access of that document
    - Can be added to whole document(then a notifictaion should be sent to all users associated with the document)
    - Can be added to any particular segment(then a notification should be sent to the Editor of that segment and owner)
    - each comment has 
        - Comment Text
        - CommentType(Bug, Conflict, Doubt, Suggestion)
        - Reply
        - Commentor Id
        - To whom the comment is(owner or the person who edited the segment)
        - Should be a comment tree like Reddit
            - eg
                ```
                comment1
                    reply1
                        reply1.1
                            reply1.1.1
                        reply1.2
                    reply2
                    ....
                ```
- Notification
    - async (through events)
    - Different Microservice
    - Sends email to the customer
    - Retry mechanism if event publishing or subscription fails
    - (Doubt) Should the notifications be sent to all the users(all Roles) associated with any document or any person can decide who can get the comment
    - Notification events should be published at
        - Comment added
        - Reply added
        - Access Requested
        - Access Provided
        - Suggest some more
- Collaboration
    - (Doubt) how should it be persisted in database
    - (Doubt) The user who has contributed to any segment of the document should be saved
    - 
- Rag Chatbot
    - another microservice
    - Can summarises document
    - Can give suggestions
    - Can answer query of that document
- Caching 
    - Caches the responses of rag
    - caches the search query
- Search engine
    - (Doubt) another microservice or same monolith
    - a global search bar
        - can search based on name of document
        - can search based on content of document
- Audit Trail
    - Another Microservice
    - Can only be accessed by Admin
    - Async
- Additional Features
    - Conflicts resolution when 2 editors made changes to same segment(Doubt: how to create this feature)
    - Versioning 
        - Any editor can publish the document
        - and once any other editor or owner has published and new value has been added then a new version can be seen
        - (Doubt) how to maintain this feature
    - A Markdown Previewer
    - A mermaid Previewer
    - Yaml Linter
    - Sharing document with role access
    - Document Searching feature (elasticSearch or pinecone)
    - A Rag Chatbot for giving summary and questions
    - Downloading the document
    - Renaming the document
    - A random Meme Name should be assigned to document name first
    - Once a document is created the user should become the owner of that document
    - Presence / Cursor Awareness (who’s viewing, live cursors)
    - Realtime typing indicators
    - Granular audit trail

## Tech Stack
    - Java
    - Spring boot
    - Redis
    - Kafka
    - Python for Rag 
    - Vue and Typescript for frontend

## Proposed Architecture
- API Gateway / Auth (BFF) — single entry point, JWT verification, rate limiting.
- User Service — user profiles, roles, SSO, authentication, user metadata.
- Document Service — core CRUD, document canonical states, versioning, storage metadata (not heavy real-time ops).
- Collaboration Service (Real-time) — real-time collaboration engine (CRDT/OT), presence, WebSocket rooms. Accepts operations and broadcasts them.
- Comment Service — comment store, threading, moderation.
- Notification Service — consumes events and sends email/push (with retry and dead-letter queues).
- Search Service — full-text + vector search (Elasticsearch for text + vector store for embeddings).
- RAG Service — embedding generation, query agent, caching; uses vector DB + LLM.
- Audit Service — append-only event store for admin access.
- File/Media Service — object storage (S3) for images, attachments, original uploaded files.
<!-- - Gateway / CDN for static assets. -->
<!-- - Orchestration: Kafka for events, Postgres for transactional storage, Redis for caching and presence. -->


# Goal
- Develop User + Document + Comment first
- Add Notification + Audit Trail
- Add Search Engine
- Add Rag Service