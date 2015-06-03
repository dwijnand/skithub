package net.mox9.octokit

import play.api.libs.functional.syntax._

import java.time._

final case class User(
  login               : String,
  id                  : Int,
  avatar_url          : Url,
  gravatar_id         : String,
  url                 : Url,
  html_url            : Url,
  followers_url       : Url,
  following_url       : Url,
  gists_url           : Url,
  starred_url         : Url,
  subscriptions_url   : Url,
  organizations_url   : Url,
  repos_url           : Url,
  events_url          : Url,
  received_events_url : Url,
  `type`              : String,
  site_admin          : Boolean
)
object User {
  implicit val jsonFormat: JsonFormat[User] = Json.format[User]
}

final case class RepoPermissions(admin: Boolean, push: Boolean, pull: Boolean)
object RepoPermissions {
  implicit val jsonFormat: JsonFormat[RepoPermissions] = Json.format[RepoPermissions]
}

final case class RepoSummary(
  id                : Long,
  owner             : User,
  name              : String,
  full_name         : String,
  description       : Option[String],
  `private`         : Boolean,
  fork              : Boolean,

  url               : Url,
  html_url          : Url,
  clone_url         : Url,
  git_url           : Url,
  ssh_url           : Url,
  svn_url           : Url,
  mirror_url        : Option[Url],
  homepage          : Option[Url],

  language          : Option[String],
  forks_count       : Int,
  stargazers_count  : Int,
  watchers_count    : Int,
  size              : Int,
  default_branch    : String,
  open_issues_count : Int,
  has_issues        : Boolean,
  has_wiki          : Boolean,
  has_pages         : Boolean,
  has_downloads     : Boolean,
  pushed_at         : ZonedDateTime,
  created_at        : ZonedDateTime,
  updated_at        : ZonedDateTime,

  permissions       : Option[RepoPermissions]
)
object RepoSummary {
  val reads1 = (
    (__ \ "id"          ) .read[Long]           and
    (__ \ "owner"       ) .read[User]           and
    (__ \ "name"        ) .read[String]         and
    (__ \ "full_name"   ) .read[String]         and
    (__ \ "description" ) .readNullable[String] and
    (__ \ "private"     ) .read[Boolean]        and
    (__ \ "fork"        ) .read[Boolean]
  ).tupled

  val reads2 = (
    (__ \ "url"        ) .read[Url] and
    (__ \ "html_url"   ) .read[Url] and
    (__ \ "clone_url"  ) .read[Url] and
    (__ \ "git_url"    ) .read[Url] and
    (__ \ "ssh_url"    ) .read[Url] and
    (__ \ "svn_url"    ) .read[Url] and
    (__ \ "mirror_url" ) .readNullable[Url] and
    (__ \ "homepage"   ) .readNullable[Url]
  ).tupled

  val reads3 = (
    (__ \ "language"          ) .readNullable[String]          and
    (__ \ "forks_count"       ) .read[Int]                     and
    (__ \ "stargazers_count"  ) .read[Int]                     and
    (__ \ "watchers_count"    ) .read[Int]                     and
    (__ \ "size"              ) .read[Int]                     and
    (__ \ "default_branch"    ) .read[String]                  and
    (__ \ "open_issues_count" ) .read[Int]                     and
    (__ \ "has_issues"        ) .read[Boolean]                 and
    (__ \ "has_wiki"          ) .read[Boolean]                 and
    (__ \ "has_pages"         ) .read[Boolean]                 and
    (__ \ "has_downloads"     ) .read[Boolean]                 and
    (__ \ "pushed_at"         ) .read[ZonedDateTime]           and
    (__ \ "created_at"        ) .read[ZonedDateTime]           and
    (__ \ "updated_at"        ) .read[ZonedDateTime]
  ).tupled

  val reads4 = (__ \ "permissions").readNullable[RepoPermissions]

  implicit val jsonReads: Reads[RepoSummary] =
    reads1 and reads2 and reads3 and reads4 apply { (v1, v2, v3, permissions) =>
      val (id, owner, name, full_name, description, private1, fork) = v1
      val (url, html_url, clone_url, git_url, ssh_url, svn_url, mirror_url, homepage) = v2
      val (
        language, fork_count, stargazers_count, watchers_count, size, default_branch, open_issues_count,
        has_issues, has_wiki, has_pages, has_downloads, pushed_at, created_at, updated_at
      ) = v3
      RepoSummary(
        id, owner, name, full_name, description, private1, fork,
        url, html_url, clone_url, git_url, ssh_url, svn_url, mirror_url, homepage,
        language, fork_count, stargazers_count, watchers_count, size, default_branch, open_issues_count,
        has_issues, has_wiki, has_pages, has_downloads, pushed_at, created_at, updated_at, permissions
      )
    }

  val writes1 =
    OWrites[RepoSummary] { rs => import rs._
      Json.obj(
        "id"                -> id,
        "owner"             -> owner,
        "name"              -> name,
        "full_name"         -> full_name,
        "description"       -> description,
        "private"           -> `private`,
        "fork"              -> fork,
        "url"               -> url,
        "html_url"          -> html_url,
        "clone_url"         -> clone_url,
        "git_url"           -> git_url,
        "ssh_url"           -> ssh_url,
        "svn_url"           -> svn_url,
        "mirror_url"        -> mirror_url,
        "homepage"          -> homepage,
        "language"          -> language,
        "forks_count"       -> forks_count,
        "stargazers_count"  -> stargazers_count,
        "watchers_count"    -> watchers_count,
        "size"              -> size,
        "default_branch"    -> default_branch,
        "open_issues_count" -> open_issues_count,
        "has_issues"        -> has_issues,
        "has_wiki"          -> has_wiki,
        "has_pages"         -> has_pages,
        "has_downloads"     -> has_downloads,
        "pushed_at"         -> pushed_at,
        "created_at"        -> created_at,
        "updated_at"        -> updated_at
      )
    }

  val writes2 = OWrites[RepoSummary](rs => Json.obj("permissions" -> rs.permissions))

  implicit val jsonWrites:     Writes[RepoSummary] = (writes1 ~ writes2).join
  implicit val jsonFormat: JsonFormat[RepoSummary] = JsonFormat(jsonReads, jsonWrites)
}

final case class Repo(
  id                : Long,
  owner             : User,
  name              : String,
  full_name         : String,
  description       : Option[String],
  `private`         : Boolean,
  fork              : Boolean,

  url               : Url,
  html_url          : Url,
  clone_url         : Url,
  git_url           : Url,
  ssh_url           : Url,
  svn_url           : Url,
  mirror_url        : Option[Url],
  homepage          : Option[Url],

  language          : Option[String],
  forks_count       : Int,
  stargazers_count  : Int,
  watchers_count    : Int,
  size              : Int,
  default_branch    : String,
  open_issues_count : Int,
  has_issues        : Boolean,
  has_wiki          : Boolean,
  has_pages         : Boolean,
  has_downloads     : Boolean,
  pushed_at         : ZonedDateTime,
  created_at        : ZonedDateTime,
  updated_at        : ZonedDateTime,

  permissions       : RepoPermissions,
  subscribers_count : Int,
  organization      : Option[User],
  parent            : Option[RepoSummary],
  source            : Option[RepoSummary]
)
object Repo {
  import RepoSummary._
  val reads4 = (
    (__ \ "permissions"       ) .read[RepoPermissions]     and
    (__ \ "subscribers_count" ) .read[Int]                 and
    (__ \ "organization"      ) .readNullable[User]        and
    (__ \ "parent"            ) .readNullable[RepoSummary] and
    (__ \ "source"            ) .readNullable[RepoSummary]
  ).tupled

  implicit val jsonReads: Reads[Repo] =
    reads1 and reads2 and reads3 and reads4 apply { (v1, v2, v3, v4) =>
      val (id, owner, name, full_name, description, private1, fork) = v1
      val (url, html_url, clone_url, git_url, ssh_url, svn_url, mirror_url, homepage) = v2
      val (
        language, fork_count, stargazers_count, watchers_count, size, default_branch, open_issues_count,
        has_issues, has_wiki, has_pages, has_downloads, pushed_at, created_at, updated_at
      ) = v3
      val (permissions, subscribers_count, organization, parent, source) = v4
      Repo(
        id, owner, name, full_name, description, private1, fork,
        url, html_url, clone_url, git_url, ssh_url, svn_url, mirror_url, homepage,
        language, fork_count, stargazers_count, watchers_count, size, default_branch, open_issues_count,
        has_issues, has_wiki, has_pages, has_downloads, pushed_at, created_at, updated_at, permissions,
        subscribers_count, organization, parent, source
      )
    }

  val writes2 =
    OWrites[Repo] { rs => import rs._
      Json.obj(
        "permissions"       -> permissions,
        "subscribers_count" -> subscribers_count,
        "organization"      -> organization,
        "parent"            -> parent,
        "source"            -> source
      )
    }

  implicit val jsonWrites: Writes[Repo] =
    (writes1 ~ writes2) { r =>
      val rs = RepoSummary(
        id                = r.id,
        owner             = r.owner,
        name              = r.name,
        full_name         = r.full_name,
        description       = r.description,
        `private`         = r.`private`,
        fork              = r.fork,

        url               = r.url,
        html_url          = r.html_url,
        clone_url         = r.clone_url,
        git_url           = r.git_url,
        ssh_url           = r.ssh_url,
        svn_url           = r.svn_url,
        mirror_url        = r.mirror_url,
        homepage          = r.homepage,

        language          = r.language,
        forks_count       = r.forks_count,
        stargazers_count  = r.stargazers_count,
        watchers_count    = r.watchers_count,
        size              = r.size,
        default_branch    = r.default_branch,
        open_issues_count = r.open_issues_count,
        has_issues        = r.has_issues,
        has_wiki          = r.has_wiki,
        has_pages         = r.has_pages,
        has_downloads     = r.has_downloads,
        pushed_at         = r.pushed_at,
        created_at        = r.created_at,
        updated_at        = r.updated_at,

        permissions       = r.permissions.some
      )
      (rs, r)
    }

  implicit val jsonFormat: JsonFormat[Repo] = JsonFormat(jsonReads, jsonWrites)
}

// Alternative listYourRepos / listUserRepos
/** @see https://developer.github.com/v3/repos/ */
final class ReposClient(gh: GitHubClient, actorSystem: ActorSystem) {
  import actorSystem.dispatcher

  def getRepos()               : Future[Seq[RepoSummary]] = getReposAtUrl(s"/user/repos")
  def getOrgRepos(org: String) : Future[Seq[RepoSummary]] = getReposAtUrl(s"/orgs/$org/repos")

  def getRepo(owner: String, repo: String): Future[Repo] =
    gh url s"/repos/$owner/$repo" get() map (_.json.as[Repo])

  def getRepoLanguage(owner: String, repo: String): Future[Map[String, Int]] =
    gh url s"/repos/$owner/$repo/languages" get() map (_.json.as[Map[String, Int]])

  private def getReposAtUrl(path: String): Future[Seq[RepoSummary]] =
    (getReposResp(path, 1)
      flatMap { resp =>
        resp.json.as[Seq[RepoSummary]] match {
          case jsError: JsError => jsError.future
          case reposJson        =>
            val remainingReposJson = resp header "Link" flatMap getPageCount match {
              case Some(pageCount) => (2 to pageCount).toVector traverse (getReposJson(path, _))
              case None            => Vector.empty.future
            }
            remainingReposJson.foldMap(reposJson)(_ |+| _)
        }
      }
    )

  private def getReposJson(path: String, pageNum: Int): Future[Seq[RepoSummary]] =
    getReposResp(path, pageNum) map (_.json.as[Seq[RepoSummary]])

  private def getReposResp(path: String, pageNum: Int): Future[WSResponse] =
    (gh
      url path
      withQueryString "page" -> s"$pageNum"
      withQueryString "sort" -> "updated"
      get()
    )

  private def getPageCount(link: String) =
    """<.+[?&]page=(\d+).*>; rel="last"""".r findFirstMatchIn link map (_ group 1 toInt)
}
