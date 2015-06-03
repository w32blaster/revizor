databaseChangeLog = {

	changeSet(author: "ilja (generated)", id: "1433353755434-1") {
		createTable(tableName: "alias") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "aliasPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "alias_email", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-2") {
		createTable(tableName: "chat") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "chatPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "channel", type: "varchar(255)")

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "password", type: "varchar(255)")

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "url", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "username", type: "varchar(255)")
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-3") {
		createTable(tableName: "comment") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "commentPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "author_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "commit", type: "varchar(255)")

			column(name: "date", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "file_name", type: "varchar(255)")

			column(name: "line_of_code", type: "integer") {
				constraints(nullable: "false")
			}

			column(name: "reply_to_id", type: "bigint")

			column(name: "review_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "text", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "type_of_line", type: "varchar(255)")
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-4") {
		createTable(tableName: "issue") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "issuePK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "key", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "review_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "tracker_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-5") {
		createTable(tableName: "issue_tracker") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "issue_trackerPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "issue_key_pattern", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "password", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "title", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "url", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "username", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-6") {
		createTable(tableName: "notification") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "notificationPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "action", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "detailed_actor_index", type: "integer") {
				constraints(nullable: "false")
			}

			column(name: "object_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "show_only_to_reciepent", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "time", type: "timestamp") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-7") {
		createTable(tableName: "notification_notification_object") {
			column(name: "notification_actors_id", type: "bigint")

			column(name: "notification_object_id", type: "bigint")
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-8") {
		createTable(tableName: "notification_object") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "notification_PK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "idx", type: "integer") {
				constraints(nullable: "false")
			}

			column(name: "object_id", type: "bigint")

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "value", type: "varchar(255)")
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-9") {
		createTable(tableName: "repository") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "repositoryPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "folder_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "has_image", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "image", type: "longvarchar")

			column(name: "image_type", type: "varchar(255)")

			column(name: "password", type: "varchar(255)")

			column(name: "title", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "url", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "username", type: "varchar(255)")
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-10") {
		createTable(tableName: "review") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "reviewPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "author_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "commits", type: "binary(255)") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "repository_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "smart_commit_id", type: "varchar(255)")

			column(name: "status", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "title", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-11") {
		createTable(tableName: "reviewer") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "reviewerPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "review_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "reviewer_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "status", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-12") {
		createTable(tableName: "unread_event") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "unread_eventPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "notification_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "object_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-13") {
		createTable(tableName: "user") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "userPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "email", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "has_image", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "image", type: "longvarchar")

			column(name: "image_type", type: "varchar(255)")

			column(name: "password", type: "varchar(255)")

			column(name: "position", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "role", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "username", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-14") {
		createTable(tableName: "user_repositories") {
			column(name: "user_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "repository_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-15") {
		addPrimaryKey(columnNames: "user_id, repository_id", tableName: "user_repositories")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-33") {
		createIndex(indexName: "email_uniq_1433353755375", tableName: "user", unique: "true") {
			column(name: "email")
		}
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-16") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "alias", constraintName: "FK_pb2dnc78jf4mogk534fx0ajub", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-17") {
		addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "comment", constraintName: "FK_9aq5p2jgf17y6b38x5ayd90oc", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-18") {
		addForeignKeyConstraint(baseColumnNames: "reply_to_id", baseTableName: "comment", constraintName: "FK_mit9v7lmh80cpsahepgoqg9pu", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "comment", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-19") {
		addForeignKeyConstraint(baseColumnNames: "review_id", baseTableName: "comment", constraintName: "FK_782vrx75iijp91is6t9qgl7rv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "review", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-20") {
		addForeignKeyConstraint(baseColumnNames: "review_id", baseTableName: "issue", constraintName: "FK_ao18vmlsb3ss5ondr0cienl9h", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "review", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-21") {
		addForeignKeyConstraint(baseColumnNames: "tracker_id", baseTableName: "issue", constraintName: "FK_ad0q1a4dhq28rxnspvprh07nt", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "issue_tracker", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-22") {
		addForeignKeyConstraint(baseColumnNames: "object_id", baseTableName: "notification", constraintName: "FK_1mqi450a3qk1x2p1nvwjmv7qr", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-23") {
		addForeignKeyConstraint(baseColumnNames: "notification_actors_id", baseTableName: "notification_notification_object", constraintName: "FK_k9hurvayt87plinjb3c59f31q", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "notification", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-24") {
		addForeignKeyConstraint(baseColumnNames: "notification_object_id", baseTableName: "notification_notification_object", constraintName: "FK_r6rtesgrpcmpxl36119kp5lgi", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "notification_object", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-25") {
		addForeignKeyConstraint(baseColumnNames: "author_id", baseTableName: "review", constraintName: "FK_dliaape2s8ix8yhsep39hicp6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-26") {
		addForeignKeyConstraint(baseColumnNames: "repository_id", baseTableName: "review", constraintName: "FK_myv6dm3lwn04kmqdx1ajxdmyg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "repository", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-27") {
		addForeignKeyConstraint(baseColumnNames: "review_id", baseTableName: "reviewer", constraintName: "FK_espomc2a4ij7h2xhmrrg8870r", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "review", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-28") {
		addForeignKeyConstraint(baseColumnNames: "reviewer_id", baseTableName: "reviewer", constraintName: "FK_7yb8j7oeu35lk5xmx79757tg4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-29") {
		addForeignKeyConstraint(baseColumnNames: "notification_id", baseTableName: "unread_event", constraintName: "FK_9mc6j689wqxmtxu7611fh9jmw", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "notification", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-30") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "unread_event", constraintName: "FK_phu69f6a27g3tuqy97gvdom6e", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-31") {
		addForeignKeyConstraint(baseColumnNames: "repository_id", baseTableName: "user_repositories", constraintName: "FK_mdxj90dkk1xkpf5224uyipfe4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "repository", referencesUniqueColumn: "false")
	}

	changeSet(author: "ilja (generated)", id: "1433353755434-32") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_repositories", constraintName: "FK_4xrp7ribjfqg285i2ks6g6eva", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}
}
